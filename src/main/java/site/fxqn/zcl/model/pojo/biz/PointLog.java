package site.fxqn.zcl.model.pojo.biz;
/*
    作者: kat-qian
    日期: 2018/6/1 19:45
*/

import lombok.Data;
import site.fxqn.zcl.model.pojo.base.BasePojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * 积分交易日志表
 */
@Data
@Table(name = "t_biz_point_log")
public class PointLog extends BasePojo {

    @Id
    private Long id;
    private Long user_id;                   // 关联的用户id
    private String bill_id;                 // 关联的账单id
    private BigDecimal point_before;        // 交易前积分点
    private BigDecimal point_after;         // 交易后积分点
    private BigDecimal trading_volume;      // 交易额，冗余字段
    private Short trading_type_id;          // 操作类型id
    private Short pay_type_id;              // 支付类型id，如果不涉及现金兑换，此处应为null
    private String trading_desc;            // 操作描述，操作类型id关联的数据的冗余字段
    private String pay_desc;                // 支付类型描述，冗余字段
    private Long operator_id;               // 操作员id
    private Short operator_type;            // 操作员类型，   0： 用户    1：操作员

}
