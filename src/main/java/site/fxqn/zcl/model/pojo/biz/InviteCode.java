package site.fxqn.zcl.model.pojo.biz;
/*
    作者: kat-qian
    日期: 2018/5/31 21:25
*/

import lombok.Data;
import site.fxqn.zcl.model.pojo.base.BasePojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 邀请码实体类
 */
@Data
@Table(name = "t_biz_invite_code")
public class InviteCode extends BasePojo {

    // 状态： -1： 被人工废弃     0：可用     1：已被使用     2：已过期
    @Id
    private Long id;
    private String invite_code;      // 邀请码
    private Long generate_user_id;   // 生成该邀请码的用户id
    private BigDecimal rebate_value; // 返利额
    private Long timeout;            // 超时时间
    private Date expire_time;        // 过期时间

}
