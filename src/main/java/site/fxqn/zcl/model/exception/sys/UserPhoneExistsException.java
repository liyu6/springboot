package site.fxqn.zcl.model.exception.sys;
/*
    作者: kat-qian
    日期: 2018/6/1 11:42
*/


import site.fxqn.zcl.model.exception.base.BaseCustomerException;

/**
 * 手机号码已经存在异常
 */
public class UserPhoneExistsException extends BaseCustomerException {
    public UserPhoneExistsException() {
        super("该手机号码已经被用于注册");
    }
}
