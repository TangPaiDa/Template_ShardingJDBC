package cn.tang.userMode.controller;


import cn.tang.common.jwt.JwtTargetForRole;
import cn.tang.common.utils.ReflectionUtil;
import cn.tang.common.utils.ResultJsonBean;
import cn.tang.userMode.entity.SysPermissionsEntity;
import cn.tang.userMode.service.SysPermissionsService;
import cn.tang.userMode.vo.SysRolePermissionsVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 权限信息表 前端控制器
 * </p>
 *
 * @author Tangpd
 * @since 2020-12-02
 */
@RestController
@RequestMapping("/userMode/sysPermissions/")
public class SysPermissionsController {

    @Resource
    private SysPermissionsService sysPermissionsService;


    /**
     *
     * 功能描述: 获取全部权限信息（树状结构）
     *
     * @param: []
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/8 13:47
     */
    @PostMapping("getAllPermissions")
    @JwtTargetForRole("admin")
    public ResultJsonBean getAllPermissions() {
        return sysPermissionsService.getAllPermissions();
    }



    /**
     *
     * 功能描述: 删除权限信息
     *
     * @param: [sysPermissionsEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/3 10:09
     */
    @PostMapping("deletePermissions")
    @JwtTargetForRole("admin")
    public ResultJsonBean deletePermissions(@RequestBody SysPermissionsEntity sysPermissionsEntity) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysPermissionsEntity, new String[]{"permissionsId"});
        return sysPermissionsService.deletePermissions(sysPermissionsEntity);
    }


    /**
     *
     * 功能描述: 修改权限信息，权限id为必传，其他字段如果传入值，就会修改数据库中相应的字段的值
     *
     * @param: [sysPermissionsEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/2 16:04
     */
    @PostMapping("updatePermissions")
    @JwtTargetForRole("admin")
    public ResultJsonBean updatePermissions(@RequestBody SysPermissionsEntity sysPermissionsEntity, HttpServletRequest request) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysPermissionsEntity, new String[]{"permissionsId"});
        return sysPermissionsService.updatePermissions(sysPermissionsEntity, request);
    }


    /**
     *
     * 功能描述: 增加权限信息
     *
     * @param: [sysPermissionsEntity]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/2 14:09
     */
    @PostMapping("addPermissions")
    @JwtTargetForRole("admin")
    public ResultJsonBean addPermissions(@RequestBody SysPermissionsEntity sysPermissionsEntity, HttpServletRequest request) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysPermissionsEntity, new String[]{"permissionsName", "hierarchy", "resourcesList", "sort", "parentId"});
        return sysPermissionsService.addPermissions(sysPermissionsEntity, request);
    }


    //=============================================================角色-权限关联相关=====================================================================

    /**
     *
     * 功能描述: 批量添加角色的权限
     *
     * @param: [sysRolePermissionsVo]
     * @return: cn.tang.common.utils.ResultJsonBean
     * @auther: tangpd
     * @date: 2020/12/2 18:21
     */
    @PostMapping("addRolePermissions")
    @JwtTargetForRole("admin")
    public ResultJsonBean addRolePermissions(@RequestBody SysRolePermissionsVo sysRolePermissionsVo) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.checkParameterHaveNull(sysRolePermissionsVo, new String[]{"roleId", "permissionsIdList"});
        return sysPermissionsService.addRolePermissions(sysRolePermissionsVo);
    }


}

