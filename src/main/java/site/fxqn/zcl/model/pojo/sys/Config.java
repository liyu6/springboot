package site.fxqn.zcl.model.pojo.sys;
/*
    作者: kat-qian
    日期: 2018/6/3 16:06
*/

import lombok.Data;
import site.fxqn.zcl.model.pojo.base.BasePojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "t_sys_config")
public class Config extends BasePojo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String config_name;
    private String config_value;
}
