package site.fxqn.zcl.web.sys;
/*
    作者: kat-qian
    日期: 2018/5/31 12:39
*/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.fxqn.zcl.model.common.ResultBody;
import site.fxqn.zcl.model.exception.base.BaseCustomerException;
import site.fxqn.zcl.model.pojo.sys.User;
import site.fxqn.zcl.service.declare.sys.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${server.session.timeout}")
    private Long verifyCodeSessionTimeout;

    @Value("${biz.verify-code-session-key}")
    private String verifyCodeSessionKey;

    @PostMapping("/post.php")
    public ResultBody post(String name){
        List<User> users=userService.findAllUser();
        log.info("users:"+users+name);
//        Map<String, User> map = new HashMap<>();
//        map.put("list",users.get(0));
        return ResultBody.success(users);
    }

    @GetMapping("/get.php")
    public JSONObject get(String name){
        List<User> users=userService.findAllUser();
        Map<String, List<User>> map = new HashMap<>();
        map.put("list",users);
        String s = JSON.toJSONString(map);
        JSONObject jsonObject = JSON.parseObject(s);
        log.info(name);
        log.info("我是map转json:"+jsonObject);
        return jsonObject;
    }

    /**
     * 发送验证码
     *
     * @param user 使用user对象进行手机参数验证
     * @return
     */
    @PostMapping("/sendVerifyCode.php")
    public ResultBody sendVerifyCode(@Validated(User.ForSendVerifyCode.class) User user, HttpServletRequest request) {
        try {
            String verifyCode = userService.sendVerifyCode(user.getPhone());
            log.info("发送了一条验证码[电话: " + user.getPhone() + ", 验证码: " + verifyCode + "]");

            // 存入session
            request.getSession().setAttribute(verifyCodeSessionKey, user.getPhone() + verifyCode);

            // 返回超时时间
            Map<String, Long> map = new HashMap<>();
            map.put("timeout", verifyCodeSessionTimeout);
            return ResultBody.success(map);

        } catch (BaseCustomerException e) {
            log.error(e.getMessage(), e);
            return ResultBody.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultBody.error("短信发送失败，请重试");
        }
    }


    /**
     * 注册
     *
     * @param user 用户信息
     * @return @Validated(User.ForRegister.class)
     */
    @PostMapping("/register.php")
    public ResultBody register(User user, String verifyCode, HttpServletRequest request) {
        // 验证码对比
        Object verifyCodeObj = request.getSession().getAttribute(verifyCodeSessionKey);
        if (verifyCodeObj == null || !verifyCodeObj.equals(user.getPhone() + verifyCode)) {
            return ResultBody.error("验证码错误，请重试");
        }

        // 注册
        try {
            userService.register(user);
            // 注册成功后清除session中保存的验证码
            request.getSession().removeAttribute(verifyCodeSessionKey);
            return ResultBody.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultBody.error("注册失败");
        }
    }


    /**
     * 登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login.php")
    public ResultBody login(@Validated(User.ForLogin.class) User user) {
        try {
            userService.login(user);
            return ResultBody.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultBody.error("登录失败");
        }
    }


    /**
     * 激活用户，该方法必须登录才能使用
     *
     * @param user
     * @return
     */
    @PostMapping("/activate.php")
    @RequiresAuthentication
    public ResultBody activate(@Validated(User.ForActivate.class) User user) {
        try {
            userService.activate(user);
            return ResultBody.success();
        } catch (BaseCustomerException e) {
            log.error(e.getMessage(), e);
            return ResultBody.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultBody.error("激活失败");
        }
    }
}
