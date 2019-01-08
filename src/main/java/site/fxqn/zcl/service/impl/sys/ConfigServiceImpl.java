package site.fxqn.zcl.service.impl.sys;
/*
    作者: kat-qian
    日期: 2018/6/3 16:26
*/

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.fxqn.zcl.dao.sys.ConfigMapper;
import site.fxqn.zcl.model.exception.sys.ConfigNameNotExistsException;
import site.fxqn.zcl.model.pojo.sys.Config;
import site.fxqn.zcl.service.declare.sys.ConfigService;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;


    /**
     * 获取对应key的配置的值
     *
     * @param configName
     * @return
     */
    @Override
    public Config getConfig(String configName) throws Exception {
        Example example = new Example(Config.class);
        example.createCriteria().andEqualTo("config_name", configName);
        Config config = configMapper.selectOneByExample(example);
        if (config == null) {
            throw new ConfigNameNotExistsException(configName);
        }
        return config;
    }


    /**
     * 写入配置
     */
    @Override
    public void setConfig(String configName, String configValue) throws Exception {
        Example example = new Example(Config.class);
        example.createCriteria().andEqualTo("config_name", configName);
        Config config = new Config();
        config.setConfig_value(configValue);
        configMapper.updateByExampleSelective(config, example);
    }
}
