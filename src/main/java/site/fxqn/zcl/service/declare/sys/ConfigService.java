package site.fxqn.zcl.service.declare.sys;
/*
    作者: kat-qian
    日期: 2018/6/3 16:24
*/

import site.fxqn.zcl.model.exception.sys.ConfigNameNotExistsException;
import site.fxqn.zcl.model.pojo.sys.Config;

public interface ConfigService {

    /**
     * 获取对应key的配置的值
     *
     * @param configName
     * @return
     */
    Config getConfig(String configName) throws Exception;


    /**
     * 写入配置
     */
    void setConfig(String configName, String configValue) throws Exception;
}
