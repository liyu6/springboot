package site.fxqn.zcl.service.declare.biz;
/*
    作者: kat-qian
    日期: 2018/6/3 11:24
*/

import site.fxqn.zcl.model.pojo.biz.InviteCodeBill;
import site.fxqn.zcl.model.pojo.biz.Point;

import java.math.BigDecimal;

/**
 * 对积分的操作必须通过pointService
 */
public interface PointService {


    /**
     * 获取用户的积分信息
     *
     * @param userId 用户id
     * @return
     */
    Point getUserPoint(Long userId) throws Exception;

    /**
     * 生成一条新的数据，该方法将在新用户注册后被调用
     *
     * @param userId
     */
    void generatePointRaw(Long userId);


    /**
     * 邀请码返利
     *
     * @param userId      需要返利的用户id
     * @param rebateValue 返利值
     */
    void rebateActivate(Long userId, BigDecimal rebateValue) throws Exception;


    /**
     * 购买邀请码
     *
     * @param inviteCodeBill
     */
    void buyInviteCode(InviteCodeBill inviteCodeBill) throws Exception;
}
