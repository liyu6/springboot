package site.fxqn.zcl.model.exception.sys;
/*
    作者: kat-qian
    日期: 2018/6/3 16:28
*/

import site.fxqn.zcl.model.exception.base.BaseCustomerException;

public class ConfigNameNotExistsException extends BaseCustomerException {
    public ConfigNameNotExistsException(String configName) {
        super("对应的配置未找到: " + configName);
    }
}
