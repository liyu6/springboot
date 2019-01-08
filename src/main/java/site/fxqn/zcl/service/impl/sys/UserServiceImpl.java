package site.fxqn.zcl.service.impl.sys;
/*
    作者: kat-qian
    日期: 2018/5/31 16:25
*/

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.fxqn.zcl.config.Constant;
import site.fxqn.zcl.dao.biz.InviteCodeMapper;
import site.fxqn.zcl.dao.sys.UserMapper;
import site.fxqn.zcl.model.exception.sys.UserInviteCodeNotExistsException;
import site.fxqn.zcl.model.exception.sys.UserInviteCodeUnusableException;
import site.fxqn.zcl.model.exception.sys.UserPhoneExistsException;
import site.fxqn.zcl.model.pojo.biz.InviteCode;
import site.fxqn.zcl.model.pojo.sys.User;
import site.fxqn.zcl.service.declare.biz.PointService;
import site.fxqn.zcl.service.declare.sys.UserService;
import site.fxqn.zcl.utils.ExecutorUtil;
import site.fxqn.zcl.utils.crypto.BCrypt;
import site.fxqn.zcl.utils.crypto.CryptoUtils;
import site.fxqn.zcl.utils.sms.AliyunSMSSender;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${biz.verify-code-length}")
    private Integer verifyCodeLength;

    @Value("${biz.activate-user-with-invite-code-queue-name}")
    private String activateUserWithInviteCodeQueueName;

    @Autowired
    private AliyunSMSSender smsSender;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InviteCodeMapper inviteCodeMapper;

    @Autowired
    private PointService pointService;

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 获取当前登录的用户
     *
     * @return
     */
    @Override
    public User getCurrentUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 发送验证码
     *
     * @param phone 手机号码
     * @return 生成的验证码
     */
    @Override
    public String sendVerifyCode(String phone) throws Exception {
        // 发送验证码前，首先判断该手机号是否已经注册
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("phone", phone);
        List<User> users = userMapper.selectByExample(example);
        if (users.size() > 0) {
            throw new UserPhoneExistsException();
        }

        String verifyCode = CryptoUtils.newVerifyCode(verifyCodeLength, CryptoUtils.CODE_TYPE_NUMBER_ONLY);
        // todo: 生产环境请取消此行代码注释
//        smsSender.sendVerifyCode(phone, verifyCode);

        // 返回验证码
        return verifyCode;
    }


    /**
     * 注册，注册的时候不需要校验用户名是否唯一
     * 使用数据库的unique键
     * 当因为用户名唯一注册失败后需要重新发送验证码，此时则会检测出
     *
     * @param user
     */
    @Override
    public void register(User user) {
        user.initPojo();
        // 将手机号码作为用户名
        user.setUsername(user.getPhone());
        // 计算带盐密码
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userMapper.insert(user);
        // 生成钱包
        pointService.generatePointRaw(user.getId());
    }


    /**
     * 登录
     *
     * @param user
     */
    @Override
    public void login(User user) {
        // 使用shiro登录
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
    }


    /**
     * 通过邀请码激活用户
     *
     * @param user
     */
    @Override
    public void activate(User user) throws Exception {
        // 获取邀请码，邀请码全大写
        String inviteCodeString = user.getInvite_code().toUpperCase();

        // 获取该邀请码
        Example example = new Example(InviteCode.class);
        example.createCriteria().andEqualTo("invite_code", inviteCodeString);
        List<InviteCode> inviteCodeList = inviteCodeMapper.selectByExample(example);

        // 如果邀请码不存在
        if (inviteCodeList.size() == 0) {
            throw new UserInviteCodeNotExistsException();
        }

        // 判断邀请码状态
        InviteCode inviteCode = inviteCodeList.get(0);
        if (!Constant.InviteCodeStatus.DEFAULT.equals(inviteCode.getStatus())) {
            throw new UserInviteCodeUnusableException();
        }

        // 判断邀请码是否过期
        Long deltaTime = new Date().getTime() - inviteCode.getCreate_time().getTime();
        if (deltaTime > inviteCode.getTimeout()) {
            inviteCode.setStatus(Constant.InviteCodeStatus.OUT_OF_DATE);
            inviteCode.updateModifyTime();
            // 更新该邀请码状态
            inviteCodeMapper.updateByPrimaryKey(inviteCode);
            // 抛出异常
            throw new UserInviteCodeUnusableException();
        }

        // 首先更新该邀请码状态
        example = new Example(InviteCode.class);
        example.createCriteria()
                .andEqualTo("status", Constant.InviteCodeStatus.DEFAULT)
                .andEqualTo("id", inviteCode.getId());
        // 修改为已经被使用
        inviteCode.setStatus(Constant.InviteCodeStatus.USED);
        // 判断更新影响行数，如果行数等于0，说明该邀请码已经被使用了
        Integer affectedRows = inviteCodeMapper.updateByExample(inviteCode, example);
        if (affectedRows == 0) {
            throw new UserInviteCodeUnusableException();
        }

        // 锁定该邀请码后修改该用户的信息
        // 获取当前用户，将用户名放入user对象，并进行更新
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();
        user.setId(currentUser.getId());
        user.setStatus(Constant.UserStatus.ACTIVE);               // 改成激活状态
        user.setInvite_user_id(inviteCode.getGenerate_user_id()); // 记录邀请人
        user.updateModifyTime();                                  // 更新修改时间
        user.setActivate_time(user.getModify_time());             // 记录激活时间
        userMapper.updateByPrimaryKeySelective(user);

        // 激活后需要对生成该邀请码的用户做返利操作，
        // 这里使用activemq，推入对应的消息队列中
        jmsTemplate.convertAndSend(activateUserWithInviteCodeQueueName, inviteCodeString);
    }

    @Override
    public List<User> findAllUser() {
        return  userMapper.selectAll();
    }
}
