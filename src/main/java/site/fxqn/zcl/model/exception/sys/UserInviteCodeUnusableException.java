package site.fxqn.zcl.model.exception.sys;
/*
    作者: kat-qian
    日期: 2018/6/1 16:05
*/

import site.fxqn.zcl.model.exception.base.BaseCustomerException;

public class UserInviteCodeUnusableException extends BaseCustomerException{
    public UserInviteCodeUnusableException() {
        super("邀请码已过期或已被使用");
    }
}
