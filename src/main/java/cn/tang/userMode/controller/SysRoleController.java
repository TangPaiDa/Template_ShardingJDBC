package cn.tang.userMode.controller;


import cn.tang.common.jwt.JwtTargetForRole;
import cn.tang.common.utils.ReflectionUtil;
import cn.tang.common.utils.ResultJsonBean;
import cn.tang.userMode.entity.SysRoleEntity;
import cn.tang.userMode.service.SysRoleService;
import cn.tang.userMode.vo.SysUserRoleVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author Tangpd
 * @since 2020-12-03
 */
@RestController
@RequestMapping("/userMode/sysRole/")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;



    /**
     *
     * 功能描述: 获取所有角色信息
     *
     * @param: []
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/8 13:40
     */
    @PostMapping("getAllRole")
    @JwtTargetForRole("admin")
    public ResultJsonBean getAllRole() {
        return sysRoleService.getAllRole();
    }

    /**
     *
     * 功能描述: 删除角色信息
     *
     * @param: [sysRoleEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/3 18:08
     */
    @PostMapping("deleteRole")
    @JwtTargetForRole("admin")
    public ResultJsonBean deleteRole(@RequestBody SysRoleEntity sysRoleEntity) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysRoleEntity, new String[]{"roleId"});
        return sysRoleService.deleteRole(sysRoleEntity);
    }


    /**
     *
     * 功能描述: 修改角色信息
     *
     * @param: [sysRoleEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/3 17:25
     */
    @PostMapping("updateRole")
    @JwtTargetForRole("admin")
    public ResultJsonBean updateRole(@RequestBody SysRoleEntity sysRoleEntity) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysRoleEntity, new String[]{"roleId"});
        return sysRoleService.updateRole(sysRoleEntity);
    }


    /**
     *
     * 功能描述: 添加角色信息
     *
     * @param: [sysRoleEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/3 17:13
     */
    @PostMapping("addRole")
    @JwtTargetForRole("admin")
    public ResultJsonBean addRole(@RequestBody SysRoleEntity sysRoleEntity) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysRoleEntity, new String[]{"roleName", "sort"});
        return sysRoleService.addRole(sysRoleEntity);
    }

    //=============================================================用户-角色关联相关=====================================================================

    /**
     *
     * 功能描述: 批量增加用户-角色关联关系
     *
     * @param: [userRoleVo]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/3 18:02
     */
    @PostMapping("addUserRole")
    @JwtTargetForRole("admin")
    public ResultJsonBean addUserRole(@RequestBody SysUserRoleVo userRoleVo) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(userRoleVo, new String[]{"userId", "roleIdList"});
        return sysRoleService.addUserRole(userRoleVo);
    }

    //=============================================================角色-权限关联相关=====================================================================

    /**
     *
     * 功能描述: 获取角色所拥有的权限列表
     *
     * @param: [sysRoleEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/14 18:00
     */
    @PostMapping("getPermissionsByRoleId")
    @JwtTargetForRole("admin")
    public ResultJsonBean getPermissionsByRoleId(@RequestBody SysRoleEntity sysRoleEntity) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysRoleEntity, new String[]{"roleId"});
        return sysRoleService.getPermissionsByRoleId(sysRoleEntity);
    }

}

