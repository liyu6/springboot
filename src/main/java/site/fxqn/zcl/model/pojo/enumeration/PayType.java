package site.fxqn.zcl.model.pojo.enumeration;
/*
    作者: kat-qian
    日期: 2018/6/1 20:10
*/


import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * t_enum_point_trading_type
 * 支付类型枚举表
 */
@Data
@Table(name = "t_enum_pay_type")
public class PayType {

    @Id
    private Short id;
    private String desc;

}
