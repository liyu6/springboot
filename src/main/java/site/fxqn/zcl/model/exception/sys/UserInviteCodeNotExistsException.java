package site.fxqn.zcl.model.exception.sys;
/*
    作者: kat-qian
    日期: 2018/6/1 16:01
*/

import site.fxqn.zcl.model.exception.base.BaseCustomerException;

public class UserInviteCodeNotExistsException extends BaseCustomerException{
    public UserInviteCodeNotExistsException() {
        super("邀请码不存在");
    }
}
