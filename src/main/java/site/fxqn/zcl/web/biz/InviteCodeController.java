package site.fxqn.zcl.web.biz;
/*
    作者: kat-qian
    日期: 2018/6/4 10:40
*/

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.fxqn.zcl.model.common.PageData;
import site.fxqn.zcl.model.common.ResultBody;
import site.fxqn.zcl.model.exception.base.BaseCustomerException;
import site.fxqn.zcl.model.pojo.biz.InviteCodePrice;
import site.fxqn.zcl.service.declare.biz.InviteCodeService;

@Slf4j
@RestController
@RequestMapping("/inviteCode")
public class InviteCodeController {

    @Autowired
    private InviteCodeService inviteCodeService;


    /**
     * 查询所有的邀请码价格套餐
     *
     * @return
     */
    @GetMapping("/searchPrices.php")
    @RequiresRoles({"active_user", "probation_user"})
    public ResultBody searchPrices(PageData<InviteCodePrice> pageData) {
        try {
            pageData.initPageInfo();
            PageData<InviteCodePrice> inviteCodePricePageData = inviteCodeService.searchPrices(pageData);
            return ResultBody.success(inviteCodePricePageData);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultBody.error();
        }
    }


    /**
     * 购买邀请码
     *
     * @param inviteCodePriceId
     * @param quantity
     * @return
     */
    @PostMapping("/buy.php")
    @RequiresRoles("active_role")
    public ResultBody buy(Long inviteCodePriceId, Integer quantity) {
        try {
            inviteCodeService.buy(inviteCodePriceId, quantity);
            return ResultBody.success();
        } catch (BaseCustomerException e) {
            log.error(e.getMessage(), e);
            return ResultBody.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultBody.error();
        }
    }
}
