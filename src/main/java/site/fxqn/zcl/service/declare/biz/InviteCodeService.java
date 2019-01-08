package site.fxqn.zcl.service.declare.biz;
/*
    作者: kat-qian
    日期: 2018/6/3 11:28
*/

import site.fxqn.zcl.model.common.PageData;
import site.fxqn.zcl.model.pojo.biz.InviteCodePrice;

public interface InviteCodeService {

    /**
     * 消息监听器，用于监听当一个邀请码码被使用后
     * 1. 给邀请方进行返利
     *
     * @param inviteCode 邀请码
     */
    void inviteCodeUsedForActivateListener(String inviteCode) throws Exception;


    /**
     * 分页查询邀请码的资费列表
     *
     * @param pageData 分页信息
     * @return
     */
    PageData<InviteCodePrice> searchPrices(PageData<InviteCodePrice> pageData);


    /**
     * 购买邀请码
     *
     * @param inviteCodePriceId 套餐id
     * @param quantity          购买的套餐数量
     */
    void buy(Long inviteCodePriceId, Integer quantity) throws Exception;
}
