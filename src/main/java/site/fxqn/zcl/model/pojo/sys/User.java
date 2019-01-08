package site.fxqn.zcl.model.pojo.sys;
/*
    作者: kat-qian
    日期: 2018/5/31 12:42
*/

import lombok.Data;
import site.fxqn.zcl.model.pojo.base.BasePojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;


/**
 * t_sys_user
 * 用户对应实体
 */
@Data
@Table(name = "t_sys_user")
public class User extends BasePojo {

    // 状态   -1：停用  0：试用  1：未激活

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long invite_user_id;  // 邀请人id
    private Date activate_time;   // 激活时间

    @Pattern(regexp = "[a-zA-Z\\d]{6}", message = "邀请码格式错误", groups = {ForActivate.class})
    @NotNull(message = "邀请码不能为空", groups = {ForActivate.class})
    private String invite_code;    // 邀请码

    @Pattern(regexp = "1[358]\\d{9}", message = "请输入正确的手机号码", groups = {ForLogin.class})
    @NotNull(message = "用户名不能为空", groups = {ForLogin.class})
    private String username;

    @Pattern(regexp = "[0-9a-zA-Z]{6,18}", message = "请使用6-18位的数字、字母组合", groups = {ForRegister.class})
    @NotNull(message = "密码不能为空", groups = {ForRegister.class, ForLogin.class})
    private String password;

    @Pattern(regexp = "1[358]\\d{9}", message = "请输入正确的手机号码", groups = {ForSendVerifyCode.class, ForRegister.class})
    @NotNull(message = "手机号码不能为空", groups = {ForSendVerifyCode.class, ForRegister.class})
    private String phone;


    // ================ 以下为校验分组 ====================

    public static interface ForSendVerifyCode {
    }

    public static interface ForRegister {
    }

    public static interface ForLogin {
    }

    public static interface ForActivate {
    }
}
