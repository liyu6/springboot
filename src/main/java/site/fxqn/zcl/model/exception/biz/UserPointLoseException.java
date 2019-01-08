package site.fxqn.zcl.model.exception.biz;
/*
    作者: kat-qian
    日期: 2018/6/3 17:04
*/

import site.fxqn.zcl.model.exception.base.BaseCustomerException;

public class UserPointLoseException extends BaseCustomerException {
    public UserPointLoseException(Long userId) {
        super("用户钱包积分信息丢失: " + userId);
    }
}
