package com.huafagroup.web.controller.system;

import java.util.List;
import java.util.Set;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.huafagroup.common.constant.Constants;
import com.huafagroup.common.core.domain.AjaxResult;
import com.huafagroup.common.core.domain.entity.SysMenu;
import com.huafagroup.common.core.domain.entity.SysUser;
import com.huafagroup.common.core.domain.model.LoginBody;
import com.huafagroup.common.core.domain.model.LoginUser;
import com.huafagroup.common.utils.ServletUtils;
import com.huafagroup.framework.web.service.SysLoginService;
import com.huafagroup.framework.web.service.SysPermissionService;
import com.huafagroup.framework.web.service.TokenService;
import com.huafagroup.system.service.ISysMenuService;

/**
 * 登录验证
 *
 * @author huafagroup
 */
@Api(tags = "登录验证", value = "登录验证接口")
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 管理后台登录
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @ApiOperation("管理后台登录")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }


    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
