package site.fxqn.zcl.service.impl.biz;
/*
    作者: kat-qian
    日期: 2018/6/3 12:06
*/

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.fxqn.zcl.config.Constant;
import site.fxqn.zcl.dao.biz.PointLogMapper;
import site.fxqn.zcl.dao.biz.PointMapper;
import site.fxqn.zcl.model.exception.biz.AccountNotEnoughException;
import site.fxqn.zcl.model.exception.biz.MultiPointOperatorException;
import site.fxqn.zcl.model.exception.biz.UserPointLoseException;
import site.fxqn.zcl.model.pojo.biz.InviteCodeBill;
import site.fxqn.zcl.model.pojo.biz.Point;
import site.fxqn.zcl.model.pojo.biz.PointLog;
import site.fxqn.zcl.service.declare.biz.PointService;
import site.fxqn.zcl.service.declare.sys.UserService;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;

@Service
@Transactional
@Slf4j
public class PointServiceImpl implements PointService {

    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private PointLogMapper pointLogMapper;

    @Autowired
    private UserService userService;


    /**
     * 获取用户的积分信息
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public Point getUserPoint(Long userId) throws Exception {
        Example example = new Example(Point.class);
        example.createCriteria().andEqualTo("user_id", userId);
        Point point = pointMapper.selectOneByExample(example);
        // 这里获取的数据除非被恶意修改，否则不可能为null
        if (point == null) {
            throw new UserPointLoseException(userId);
        }
        return point;
    }


    /**
     * 生成一条新的数据，该方法将在新用户注册后被调用
     *
     * @param userId
     */
    @Override
    public void generatePointRaw(Long userId) {
        Point point = new Point();
        point.setAccount(BigDecimal.ZERO);
        point.setUser_id(userId);
        point.setVersion(0L);
        point.initPojo();
        pointMapper.insert(point);
    }


    /**
     * 邀请码返利
     *
     * @param userId 需要返利的用户id
     */
    @Override
    public void rebateActivate(Long userId, BigDecimal rebateValue) throws Exception {
        // 1. 获取邀请人积分信息
        // 2. 修改邀请人余额
        // 3. 记录日志


        Point point = this.getUserPoint(userId);

        BigDecimal account = point.getAccount();
        BigDecimal accountAfter = account.add(rebateValue);
        Long pointVersion = point.getVersion();
        point.updateModifyTime();
        point.setAccount(accountAfter);
        point.setVersion(pointVersion + 1);
        Example example = new Example(Point.class);
        example.createCriteria()
                .andEqualTo("version", pointVersion)
                .andEqualTo("id", point.getId());

        // 乐观锁检测
        int affectedRows = pointMapper.updateByExampleSelective(point, example);
        if (affectedRows == 0) {
            throw new MultiPointOperatorException(userId);
        }

        PointLog pointLog = new PointLog();
        pointLog.initPojo();
        pointLog.setUser_id(userId);
        pointLog.setPoint_before(account);
        pointLog.setTrading_volume(rebateValue);
        pointLog.setPoint_after(accountAfter);
        pointLog.setTrading_type_id(Constant.TradingType.ID_ACTIVATE_REBATE);
        pointLog.setTrading_desc(Constant.TradingType.DESC_ACTIVATE_REBATE);
        pointLog.setOperator_type(Constant.OperatorType.SYSTEM);
        pointLogMapper.insertSelective(pointLog);
    }


    /**
     * 购买邀请码
     *
     * @param inviteCodeBill
     */
    @Override
    public void buyInviteCode(InviteCodeBill inviteCodeBill) throws Exception {
        // 1. 获取邀请人积分信息
        // 2. 修改邀请人余额
        // 3. 记录日志

        Point point = this.getUserPoint(userService.getCurrentUser().getId());
        BigDecimal account = point.getAccount();
        BigDecimal accountAfter = point.getAccount().subtract(inviteCodeBill.getTotal());
        Long pointVersion = point.getVersion();

        // 判断余额是否足够支付
        if (accountAfter.compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountNotEnoughException();
        }

        point.updateModifyTime();
        point.setVersion(pointVersion + 1);
        point.setAccount(accountAfter);
        Example example = new Example(Point.class);
        example.createCriteria()
                .andEqualTo("version", pointVersion)
                .andEqualTo("id", point.getId());
        // 乐观锁检测
        int affectedRows = pointMapper.updateByExampleSelective(point, example);
        if (affectedRows == 0) {
            throw new MultiPointOperatorException(point.getUser_id());
        }

        PointLog pointLog = new PointLog();
        pointLog.initPojo();
        pointLog.setUser_id(point.getUser_id());
        pointLog.setPoint_before(account);
        pointLog.setTrading_volume(inviteCodeBill.getTotal());
        pointLog.setPoint_after(accountAfter);
        pointLog.setBill_id(inviteCodeBill.getId().toString());

        pointLog.setTrading_type_id(Constant.TradingType.ID_BUY_INVITE_CODE);
        pointLog.setTrading_desc(Constant.TradingType.DESC_BUY_INVITE_CODE);
        pointLog.setOperator_type(Constant.OperatorType.USER);
        pointLog.setOperator_id(point.getUser_id());
        pointLogMapper.insertSelective(pointLog);
    }
}
