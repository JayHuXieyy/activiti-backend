package com.huafagroup.framework.web.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huafagroup.system.service.ISysPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.huafagroup.common.core.domain.entity.SysUser;
import com.huafagroup.common.core.domain.model.LoginUser;
import com.huafagroup.common.enums.UserStatus;
import com.huafagroup.common.exception.BaseException;
import com.huafagroup.common.utils.StringUtils;
import com.huafagroup.system.service.ISysUserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户验证处理
 *
 * @author huafagroup
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private ISysPostService sysPostService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        if(!user.getUserName().equals(username)){
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails loadUserByOwnerCodeAndName(String ownerCode, String ownerName, String type) throws UsernameNotFoundException {
        // 集合 =  in()  ＞  条件
        //  set() = 要设置的字段
        SysUser user = null;
        if (0 == Integer.parseInt(type)) {
            user = userService.selectUserByOwnerCode(ownerCode);
            if (StringUtils.isNull(user)) {
                log.info("登录用户：{} 不存在.", ownerCode);
                throw new BaseException("对不起，您的账号非管理员权限");
            }
        } else if (1 == Integer.parseInt(type)) {
            user = new SysUser();
            user.setOwnerCode(ownerCode);
            user.setUserName(ownerName);
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", ownerCode);
            throw new BaseException("对不起，您的账号已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", ownerCode);
            throw new BaseException("对不起，您的账号已停用");
        }
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        Set<String> postCode = sysPostService.selectPostCodeByUserId(user.getUserId());
        postCode = postCode.parallelStream().map( s ->  "GROUP_" + s).collect(Collectors.toSet());
        postCode.add("ROLE_ACTIVITI_USER");
        List<SimpleGrantedAuthority> collect = postCode.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
        return new LoginUser(user, permissionService.getMenuPermission(user),collect);
    }


}
