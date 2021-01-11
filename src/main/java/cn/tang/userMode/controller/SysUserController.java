package cn.tang.userMode.controller;


import cn.tang.common.jwt.JwtTargetForRole;
import cn.tang.common.utils.ReflectionUtil;
import cn.tang.common.utils.ResultJsonBean;
import cn.tang.userMode.entity.SysUserEntity;
import cn.tang.userMode.service.SysPermissionsService;
import cn.tang.userMode.service.SysRoleService;
import cn.tang.userMode.service.SysUserService;
import cn.tang.userMode.vo.SysUserVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统用户信息表 前端控制器
 * </p>
 *
 * @author Tangpd
 * @since 2020-10-22
 */
@RestController
@RequestMapping("/userMode/sysUser/")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysPermissionsService sysPermissionsService;

    /**
     *
     * 功能描述: 分页获取系统管理用户数据
     *
     * @param: [sysUserVo]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/1 15:16
     */
    @PostMapping("getSysUserList")
    @JwtTargetForRole("admin")
    public ResultJsonBean getSysUserList(@RequestBody SysUserVo sysUserVo) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysUserVo, new String[]{"pageSize", "pageNum", "type"});
        return sysUserService.getSysUserList(sysUserVo);
    }

    /**
     *
     * 功能描述: 修改用户信息
     * 包括重置密码、删除用户等；删除用户为逻辑删除
     *
     * @param: [sysUserVo]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/10/23 13:35
     */
    @PostMapping("updateSysUser")
    public ResultJsonBean updateSysUser(@RequestBody SysUserVo sysUserVo) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysUserVo, new String[]{"userId"});
        return sysUserService.updateSysUser(sysUserVo);
    }

    /**
     *
     * 功能描述: 增加后台管理系统用户
     *
     * @param: [sysUserEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/10/23 9:27
     */
    @PostMapping("addSysUser")
    public ResultJsonBean addSysUser(@RequestBody SysUserEntity sysUserEntity, HttpServletRequest request) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysUserEntity, new String[]{"userName", "phone", "password", "type"});
        return sysUserService.addSysUser(sysUserEntity, request);
    }


    //=============================================================认证、授权相关====================================================================


    /**
     *
     * 功能描述: 获取用户拥有的权限
     *
     * @param: []
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/8 10:30
     */
    @PostMapping("getUserPermissionsList")
    @JwtTargetForRole("admin")
    public ResultJsonBean getUserPermissionsList(@RequestBody SysUserVo sysUserVo) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysUserVo, new String[]{"userId"});
        return sysPermissionsService.getUserPermissionsList(sysUserVo);
    }


    /**
     *
     * 功能描述: 获取用户拥有的角色
     *
     * @param: []
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/4 14:21
     */
    @PostMapping("getUserRoleList")
    @JwtTargetForRole("admin")
    //@JwtTargetForPermissions("getUserRole")
    public ResultJsonBean getUserRoleList(@RequestBody SysUserVo sysUserVo) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysUserVo, new String[]{"userId"});
        return sysRoleService.getUserRoleList(sysUserVo);
    }


    /**
     *
     * 功能描述: 用户登录
     *
     * @param: [sysUserEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/10/23 15:08
     */
    @PostMapping("login")
    public ResultJsonBean login(@RequestBody SysUserEntity sysUserEntity) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysUserEntity, new String[]{"userName", "password"});
        return sysUserService.login(sysUserEntity);
    }


}

