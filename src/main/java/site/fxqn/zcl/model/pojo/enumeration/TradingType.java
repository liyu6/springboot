package site.fxqn.zcl.model.pojo.enumeration;
/*
    作者: kat-qian
    日期: 2018/6/1 20:06
*/

import lombok.Data;
import site.fxqn.zcl.model.pojo.base.BasePojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * t_enum_point_trading_type
 * 交易类型枚举表
 */
@Data
@Table(name = "t_enum_trading_type")
public class TradingType extends BasePojo {

    @Id
    private Short id;
    private String desc;     // 描述

}
