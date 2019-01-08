package site.fxqn.zcl.service.declare.sys;
/*
    作者: kat-qian
    日期: 2018/5/31 16:24
*/

import site.fxqn.zcl.model.pojo.sys.User;

import java.util.List;

public interface UserService {


    /**
     * 获取当前登录的用户
     * @return
     */
    User getCurrentUser();

    /**
     * 发送验证码
     *
     * @param phone 手机号码
     * @return 生成的验证码
     */
    String sendVerifyCode(String phone) throws Exception;


    /**
     * 注册，注册的时候不需要校验用户名是否唯一
     * 使用数据库的unique键
     * 当因为用户名唯一注册失败后需要重新发送验证码，此时则会检测出
     *
     * @param user
     */
    void register(User user);


    /**
     * 登录
     *
     * @param user
     */
    void login(User user) ;


    /**
     * 通过邀请码激活用户
     *
     * @param user
     */
    void activate(User user) throws Exception;

    List<User> findAllUser();
}
