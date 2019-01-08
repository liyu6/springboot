package site.fxqn.zcl.model.pojo.biz;
/*
    作者: kat-qian
    日期: 2018/6/4 10:32
*/

import lombok.Data;
import site.fxqn.zcl.model.pojo.base.BasePojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "t_biz_invite_code_price")
public class InviteCodePrice extends BasePojo {

    // 状态: -1: 禁用   0: 使用中

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;                   // 数量
    private BigDecimal total_price;             // 总价格
    private BigDecimal rebate_value;            // 单条邀请码返利
    private Long timeout;                       // 过期时限
    private String timeout_desc;                // 过期时限描述，如"3天"
}
