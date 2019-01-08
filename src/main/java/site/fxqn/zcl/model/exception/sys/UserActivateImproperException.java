package site.fxqn.zcl.model.exception.sys;
/*
    作者: kat-qian
    日期: 2018/6/4 10:07
*/

import site.fxqn.zcl.model.exception.base.BaseCustomerException;

public class UserActivateImproperException extends BaseCustomerException {
    public UserActivateImproperException(Long userId, String inviteCodeString) {
        super("用户激活状态异常，找不到对应的邀请码. [用户ID: " + userId + ", 邀请码: " + inviteCodeString + "]");
    }
}
