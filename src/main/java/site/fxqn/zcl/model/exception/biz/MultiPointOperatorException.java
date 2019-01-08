package site.fxqn.zcl.model.exception.biz;
/*
    作者: kat-qian
    日期: 2018/6/3 17:57
*/


import site.fxqn.zcl.model.exception.base.BaseCustomerException;

public class MultiPointOperatorException extends BaseCustomerException {
    public MultiPointOperatorException(Long userId) {
        super("多个请求对同一个账户的余额进行操作: " + userId);
    }
}
