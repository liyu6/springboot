package site.fxqn.zcl.model.pojo.biz;
/*
    作者: kat-qian
    日期: 2018/6/4 11:55
*/

import lombok.Data;
import site.fxqn.zcl.model.pojo.base.BasePojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_invite_code_bill")
public class InviteCodeBill extends BasePojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long invite_code_price_id;       // 资费数据id
    private Integer quantity;                // 单次购入的数量
    private BigDecimal total;                // 单次总价

}
