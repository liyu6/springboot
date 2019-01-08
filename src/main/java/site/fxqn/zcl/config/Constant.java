package site.fxqn.zcl.config;
/*
    作者: kat-qian
    日期: 2018/5/30 17:40
*/

public class Constant {

    /**
     * ResultBody的状态码
     */
    public static class ResultBodyCode {
        public static final Integer ERROR = 500;
        public static final Integer SUCCESS = 0;
    }


    /**
     * ResultBody的错误信息
     */
    public static class ResultBodyMessage {
        public static final String ERROR = "网络错误";
    }


    /**
     * BasePojo的状态信息
     */
    public static class PojoStatus {
        public static final Short DELETED = -1;     // 已删除
        public static final Short DEFAULT = 0;      // 正常
    }


    /**
     * User的状态信息
     */
    public static class UserStatus extends PojoStatus {
        public static final Short ACTIVE = 1;       // 已激活
        public static final Short IMPROPER = -2;    // 激活异常
    }


    /**
     * 邀请码的状态信息
     */
    public static class InviteCodeStatus extends PojoStatus {
        public static final Short OUT_OF_DATE = 2;   // 已过期
        public static final Short USED = 1;          // 已被使用
    }


    /**
     * 邀请码价格的状态信息
     */
    public static class InviteCodePriceStatus extends PojoStatus {
    }


    /**
     * 交易类型
     */
    public static class TradingType {
        public static final Short ID_ACTIVATE_REBATE = 0;
        public static final String DESC_ACTIVATE_REBATE = "激活返利";
        public static final Short ID_BUY_INVITE_CODE = 1;
        public static final String DESC_BUY_INVITE_CODE = "购买邀请码";
    }


    public static class OperatorType {
        public static final Short SYSTEM = 0;
        public static final Short ADMIN = 1;
        public static final Short USER = 2;
    }


    /**
     * 业务参数
     */
    public static class BizArgs {
        public static final Long USER_PROBATION_TIMEOUT = 30 * 60 * 1000L;    // 邀请码过期时间
    }
}
