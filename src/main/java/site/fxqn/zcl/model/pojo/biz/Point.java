package site.fxqn.zcl.model.pojo.biz;
/*
    作者: kat-qian
    日期: 2018/6/1 19:33
*/

import lombok.Data;
import site.fxqn.zcl.model.pojo.base.BasePojo;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.math.BigDecimal;


/**
 * t_biz_point
 * 积分表
 */
@Data
@Table(name = "t_biz_point")
public class Point extends BasePojo {

    @Id
    private Long id;
    private Long user_id;         // 关联的用户id
    private BigDecimal account;   // 余额
    private Long version;         // 数据版本号

}
