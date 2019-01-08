package site.fxqn.zcl.model.exception.biz;
/*
    作者: kat-qian
    日期: 2018/6/4 15:20
*/

import site.fxqn.zcl.model.exception.base.BaseCustomerException;

public class AccountNotEnoughException extends BaseCustomerException {
    public AccountNotEnoughException() {
        super("余额不足");
    }
}
