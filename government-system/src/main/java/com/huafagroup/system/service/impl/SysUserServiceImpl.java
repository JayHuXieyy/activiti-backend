package com.huafagroup.system.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huafagroup.common.constant.Constants;

import com.huafagroup.common.core.domain.entity.SysDept;
import com.huafagroup.common.exception.ParamException;
import com.huafagroup.common.utils.QueryDto;
import com.huafagroup.common.utils.SearchQueryEnum;
import com.huafagroup.common.utils.http.HttpUtils;
import com.huafagroup.system.domain.dto.SysUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.huafagroup.common.annotation.DataScope;
import com.huafagroup.common.constant.UserConstants;
import com.huafagroup.common.core.domain.entity.SysRole;
import com.huafagroup.common.core.domain.entity.SysUser;
import com.huafagroup.common.exception.CustomException;
import com.huafagroup.common.utils.SecurityUtils;
import com.huafagroup.common.utils.StringUtils;
import com.huafagroup.system.domain.SysPost;
import com.huafagroup.system.domain.SysUserPost;
import com.huafagroup.system.domain.SysUserRole;
import com.huafagroup.system.mapper.SysPostMapper;
import com.huafagroup.system.mapper.SysRoleMapper;
import com.huafagroup.system.mapper.SysUserMapper;
import com.huafagroup.system.mapper.SysUserPostMapper;
import com.huafagroup.system.mapper.SysUserRoleMapper;
import com.huafagroup.system.service.ISysConfigService;
import com.huafagroup.system.service.ISysUserService;

/**
 * 用户 业务层处理
 *
 * @author huafagroup
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysDeptServiceImpl sysDeptService;


    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    @Override
    public Page<SysUserDto> findPageList(QueryDto queryDto) throws ParseException {
        Page<SysUserDto> page = new Page<>();
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select().eq(SysUser::getDelFlag, 0);

        // 用户名称查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.USER_NAME.getValue()) != null) {
            lambdaQueryWrapper.eq(SysUser::getUserName, Long.parseLong(String.valueOf(queryDto.getSearchValue().get(SearchQueryEnum.USER_NAME.getValue()))));
        }
        // 部门查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.DEPT.getValue()) != null) {
            lambdaQueryWrapper.eq(SysUser::getDeptId, Long.parseLong(String.valueOf(queryDto.getSearchValue().get(SearchQueryEnum.DEPT.getValue()))));
        }
        // 用户状态查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.STATUS.getValue()) != null) {
            lambdaQueryWrapper.eq(SysUser::getStatus, Integer.valueOf(String.valueOf(queryDto.getSearchValue().get(SearchQueryEnum.STATUS.getValue()))));
        }
        // 用户角色查询
        if (queryDto.getSearchValue().get(SearchQueryEnum.ROLE_ID.getValue()) != null) {
            LambdaQueryWrapper<SysUserRole> userRoleQuery = Wrappers.lambdaQuery();
            userRoleQuery.eq(SysUserRole::getRoleId, Long.parseLong(String.valueOf(queryDto.getSearchValue().get(SearchQueryEnum.ROLE_ID.getValue()))));
            List<SysUserRole> userRoles = userRoleMapper.selectList(userRoleQuery);
            List<Long> userIds = userRoles.stream().map(SysUserRole::getUserId).collect(Collectors.toList());
            lambdaQueryWrapper.in(SysUser::getUserId, userIds);
        }

        Page<SysUser> userPage = userMapper.selectPage(new Page<>(queryDto.getPageNo(), queryDto.getPageSize()), lambdaQueryWrapper);
        if (userPage != null) {
            BeanUtils.copyProperties(userPage, page);
            List<SysUserDto> list = new ArrayList<>();
            List<SysUser> records = userPage.getRecords();
            if (records.size() > 0) {
                List<Integer> roleIds;
                List<SysRole> roles;
                LambdaQueryWrapper<SysRole> roleQuery = Wrappers.lambdaQuery();
                for (SysUser item : records) {
                    SysUserDto dto = new SysUserDto();
                    BeanUtils.copyProperties(item, dto);
                    //获取角色id
                    roleIds = roleMapper.selectRoleListByUserId(item.getUserId());
                    //获取角色
                    if (roleIds.size() > 0) {
                        roleQuery.in(SysRole::getRoleId, roleIds);
                        roles = roleMapper.selectList(roleQuery);
                        dto.setRoleList(roles);
                    }
                    list.add(dto);
                    roleQuery.clear();
                }
            }
            page.setRecords(list);
        }
        return page;


    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过身份证查询用户
     *
     * @param ownerCode 身份证
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByOwnerCode(String ownerCode) {
        return userMapper.selectUserByOwnerCode(ownerCode);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @param b      请求是否来自管理后台
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId, boolean b) {
        SysUser sysUser = userMapper.selectUserById(userId);
        // 身份证脱敏
        if (b) {
            sysUser.setOwnerCode(sysUser.getOwnerCode().replaceAll("(\\w{10})\\w*(\\w{3})", "$1*********$2"));
        }
        return sysUser;
    }

    @Override
    public List<SysUser> selectUserByIds(List<Long> userIds) {
        List<SysUser> list = new ArrayList<>();
        if (userIds != null) {
            userIds.forEach(userId -> {
                SysUser sysUser = userMapper.selectUserById(userId);
                list.add(sysUser);
            });
        }
        return list;
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public List<String> selectUserNameByPostCodeAndDeptId(String postCode, Long deptId) {
        return userMapper.selectUserNameByPostCodeAndDeptId(postCode, deptId);
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验身份证是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkOwnerCodeUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.selectUserByOwnerCode(user.getOwnerCode());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            LambdaQueryWrapper<SysRole> roleQuery = Wrappers.lambdaQuery();
            roleQuery.eq(SysRole::getRoleName, "审批负责人");
            //获取审批负责人的角色实体
            SysRole role = roleMapper.selectOne(roleQuery);
            roleQuery.clear();
            for (Long roleId : roles) {
                if (role.getRoleId().equals(roleId)) {
                    //新增用户角色包含审批负责人
                    //判断是否已存在审批负责人
                    SysDept sysDept = sysDeptService.getById(user.getDeptId());
                    if (sysDept.getActivitiLeader() != null) {
                        throw new ParamException("当前部门已存在审批负责人");
                    } else {
                        //新增用户为审批负责人角色同步修改部门表
                        LambdaUpdateWrapper<SysDept> updateWrapper = Wrappers.lambdaUpdate();
                        updateWrapper.eq(SysDept::getDeptId, sysDept.getDeptId())
                                .set(SysDept::getActivitiLeader, user.getUserName());
                        sysDeptService.update(updateWrapper);
                    }
                    break;
                }
            }
        }

        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, ParamException.class})
    public int updateUser(SysUser user) {
        SysDept sysDept = sysDeptService.getById(user.getDeptId());
        Boolean flag = false;//判断roles是否含有审批负责人的标识
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            LambdaQueryWrapper<SysRole> roleQuery = Wrappers.lambdaQuery();
            roleQuery.eq(SysRole::getRoleName, "审批负责人");
            //获取审批负责人角色实体
            SysRole role = roleMapper.selectOne(roleQuery);
            roleQuery.clear();
            for (Long roleId : roles) {
                if (role.getRoleId().equals(roleId)) {
                    //修改后的信息包含审批负责人角色
                    flag = true;
                    //判断是否已存在审批负责人且被修改用户是否为审批负责人
                    if (sysDept.getActivitiLeader() != null && !sysDept.getActivitiLeader().equals(user.getUserName())) {
                        throw new ParamException("当前部门已存在审批负责人");
                    } else if (sysDept.getActivitiLeader() == null) {
                        //将部门审批负责人设置为被修改用户
                        LambdaUpdateWrapper<SysDept> updateWrapper = Wrappers.lambdaUpdate();
                        updateWrapper.eq(SysDept::getDeptId, sysDept.getDeptId())
                                .set(SysDept::getActivitiLeader, user.getUserName());
                        sysDeptService.update(updateWrapper);
                    }
                    break;
                }
            }
        }
        if (sysDept.getActivitiLeader() != null && sysDept.getActivitiLeader().equals(user.getUserName()) && !flag) {
            //被修改后用户失去审批负责人角色同步修改部门表
            LambdaUpdateWrapper<SysDept> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(SysDept::getDeptId, sysDept.getDeptId())
                    .set(SysDept::getActivitiLeader, null);
            sysDeptService.update(updateWrapper);
        }

        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 删除用户信息
     *
     * @param userIds 用户id
     * @return 结果
     */
    @Override
    @Transactional
    public int removeUser(Long[] userIds) {
        for (Long userId : userIds) {
            SysUser user = userMapper.selectUserById(userId);
            //若用户为审批负责人则执行清除
            SysDept sysDept = sysDeptService.getById(user.getDeptId());
            if (sysDept.getActivitiLeader() != null && sysDept.getActivitiLeader().equals(user.getUserName())) {
                LambdaUpdateWrapper<SysDept> updateWrapper = Wrappers.lambdaUpdate();
                updateWrapper.eq(SysDept::getDeptId, sysDept.getDeptId())
                        .set(SysDept::getActivitiLeader, null);
                sysDeptService.update(updateWrapper);
            }
        }
        LambdaQueryWrapper<SysUserRole> userRoleLambdaQueryWrapper = Wrappers.lambdaQuery();
        userRoleLambdaQueryWrapper.in(SysUserRole::getUserId, userIds);
        userRoleMapper.delete(userRoleLambdaQueryWrapper);
        LambdaQueryWrapper<SysUserPost> userPostLambdaQueryWrapper = Wrappers.lambdaQuery();
        userPostLambdaQueryWrapper.in(SysUserPost::getUserId, userIds);
        userPostMapper.delete(userPostLambdaQueryWrapper);

        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.in(SysUser::getUserId,userIds);
        return userMapper.delete(userLambdaQueryWrapper);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
