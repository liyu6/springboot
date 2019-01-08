package site.fxqn.zcl.service.impl.biz;
/*
    作者: kat-qian
    日期: 2018/6/3 11:30
*/

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.fxqn.zcl.config.Constant;
import site.fxqn.zcl.dao.biz.InviteCodeBillMapper;
import site.fxqn.zcl.dao.biz.InviteCodeMapper;
import site.fxqn.zcl.dao.biz.InviteCodePriceMapper;
import site.fxqn.zcl.dao.sys.UserMapper;
import site.fxqn.zcl.model.common.PageData;
import site.fxqn.zcl.model.exception.biz.InviteCodeNotUseException;
import site.fxqn.zcl.model.exception.sys.UserActivateImproperException;
import site.fxqn.zcl.model.pojo.biz.InviteCode;
import site.fxqn.zcl.model.pojo.biz.InviteCodeBill;
import site.fxqn.zcl.model.pojo.biz.InviteCodePrice;
import site.fxqn.zcl.model.pojo.sys.User;
import site.fxqn.zcl.service.declare.biz.InviteCodeService;
import site.fxqn.zcl.service.declare.biz.PointService;
import site.fxqn.zcl.utils.crypto.CryptoUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;


@Service
@Slf4j
@Transactional
public class InviteCodeServiceImpl implements InviteCodeService {

    @Autowired
    private PointService pointService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InviteCodeMapper inviteCodeMapper;

    @Autowired
    private InviteCodePriceMapper inviteCodePriceMapper;

    @Autowired
    private InviteCodeBillMapper inviteCodeBillMapper;


    /**
     * 消息监听器，用于监听当一个邀请码码被使用后
     * 1. 给邀请方进行返利
     *
     * @param inviteCodeString 邀请码
     */
    @Override
    @JmsListener(destination = "activateInviteCode")
    public void inviteCodeUsedForActivateListener(String inviteCodeString) throws Exception {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("invite_code", inviteCodeString);
        User user = userMapper.selectOneByExample(example);

        // 如果没有对应的激活的用户
        if (user == null) {
            throw new InviteCodeNotUseException(inviteCodeString);
        }

        // 找到对应的邀请码对象
        example = new Example(InviteCode.class);
        example.createCriteria().andEqualTo("invite_code", inviteCodeString);
        InviteCode inviteCode = inviteCodeMapper.selectOneByExample(example);
        if (inviteCode == null) {
            user.setStatus(Constant.UserStatus.IMPROPER);
            user.updateModifyTime();
            userMapper.updateByPrimaryKeySelective(user);
            throw new UserActivateImproperException(user.getId(), inviteCodeString);
        }

        //bug renaire:这里原来写的是user.getId(),这里应该是生成邀请码用户的id
        pointService.rebateActivate(inviteCode.getGenerate_user_id(), inviteCode.getRebate_value());
    }


    /**
     * 分页查询邀请码的资费列表
     *
     * @param pageData 分页信息
     * @return
     */
    @Override
    public PageData<InviteCodePrice> searchPrices(PageData<InviteCodePrice> pageData) {
        PageHelper.startPage(pageData.getPage(), pageData.getPageSize());
        Example example = new Example(InviteCodePrice.class);
        example.createCriteria().andEqualTo("status", Constant.InviteCodePriceStatus.DEFAULT);
        Page<InviteCodePrice> inviteCodePrices = (Page<InviteCodePrice>) inviteCodePriceMapper.selectByExample(example);
        return PageData.fromPage(inviteCodePrices);
    }


    /**
     * 购买邀请码
     *
     * @param inviteCodePriceId 套餐id
     * @param quantity          购买的套餐数量
     */
    @Override
    public void buy(Long inviteCodePriceId, Integer quantity) throws Exception {
        // 1. 查询出对应的套餐信息
        // 2. 生成账单信息
        // 3. 调用pointService进行交易
        // 3. 循环生成邀请码并进行插入

        InviteCodePrice inviteCodePrice = inviteCodePriceMapper.selectByPrimaryKey(inviteCodePriceId);

        InviteCodeBill inviteCodeBill = new InviteCodeBill();
        inviteCodeBill.setInvite_code_price_id(inviteCodePriceId);
        inviteCodeBill.setQuantity(quantity);
        inviteCodeBill.setTotal(inviteCodePrice.getTotal_price().multiply(BigDecimal.valueOf(quantity)));
        inviteCodeBillMapper.insert(inviteCodeBill);

        pointService.buyInviteCode(inviteCodeBill);

        // 计算一共要生成多少邀请码
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        Integer inviteCodeNum = inviteCodePrice.getQuantity() * quantity;
        InviteCode inviteCode = new InviteCode();
        inviteCode.initPojo();
        inviteCode.setGenerate_user_id(currentUser.getId());
        inviteCode.setRebate_value(inviteCodePrice.getRebate_value());
        inviteCode.setTimeout(inviteCodePrice.getTimeout());
        Long expireTimeTimestamp = inviteCode.getCreate_time().getTime() + inviteCode.getTimeout();
        inviteCode.setExpire_time(new Date(expireTimeTimestamp));
        for (int i = 0; i < inviteCodeNum; i++) {
            inviteCode.setId(null);
            inviteCode.setInvite_code(CryptoUtils.newVerifyCode(6, CryptoUtils.CODE_TYPE_ALL));
            // 这里可能出现邀请码重复的问题
            try {
                inviteCodeMapper.insert(inviteCode);
            } catch (Exception e) {
                log.error("生成邀请码添加到数据库时错误: " + inviteCode.getInvite_code());
                log.error(e.getMessage(), e);
                i--;
            }
        }
    }
}
