package site.fxqn.zcl.config.shiro;
/*
    作者: kat-qian
    日期: 2018/5/31 11:23
*/

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.fxqn.zcl.dao.sys.UserMapper;
import site.fxqn.zcl.config.Constant;
import site.fxqn.zcl.model.pojo.sys.User;
import site.fxqn.zcl.utils.SpringUtils;
import site.fxqn.zcl.utils.crypto.BCrypt;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * shiro配置的realm，提供授权和认证两个方法
 */

@Component
public class ShiroRealm extends AuthorizingRealm implements Realm {

    @Autowired
    private UserMapper userMapper;


    /**
     * 授权，在该系统下，只需要用户有"正式用户角色"，即status为1
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        // 创建授权信息对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 获取该用户的status
        User resultUser = userMapper.selectByPrimaryKey(user.getId());

        // 如果用户已经激活
        if (resultUser.getStatus() != null && resultUser.getStatus().equals(Constant.UserStatus.ACTIVE)) {
            info.addRole("active_user");
        }

        // 如果用户处于试用期
        long createTimestamp = resultUser.getCreate_time().getTime();
        long nowTimestamp = new Date().getTime();
        long deltaTimestamp = nowTimestamp - createTimestamp;
        if (deltaTimestamp < Constant.BizArgs.USER_PROBATION_TIMEOUT) {
            info.addRole("probation_user");
        }

        return info;
    }


    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Example example = new Example(User.class);
        example.createCriteria()
                .andEqualTo("username", token.getUsername())
                .andNotEqualTo("status", Constant.UserStatus.DELETED);

        // 根据用户名查找用户
        List<User> users = userMapper.selectByExample(example);
        if (users.size() == 0) {
            return null;
        }

        // 比对密码
        User user = users.get(0);
        String password = new String(token.getPassword());
        if (!BCrypt.checkpw(password, user.getPassword())) {
            return new SimpleAuthenticationInfo();
        }

        // 登录成功
        return new SimpleAuthenticationInfo(user, password, getName());
    }
}
