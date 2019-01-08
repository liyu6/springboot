package site.fxqn.zcl.model.exception.biz;
/*
    作者: kat-qian
    日期: 2018/6/3 17:01
*/

import site.fxqn.zcl.model.exception.base.BaseCustomerException;

public class InviteCodeNotUseException extends BaseCustomerException {
    public InviteCodeNotUseException(String inviteCodeString) {
        super("要求对未使用或不存在的激活码进行返利: " + inviteCodeString);
    }
}
