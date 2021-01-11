package cn.tang.userMode.service;

import cn.tang.common.utils.ResultJsonBean;
import cn.tang.userMode.entity.SysPermissionsEntity;
import cn.tang.userMode.vo.SysRolePermissionsVo;
import cn.tang.userMode.vo.SysUserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 权限信息表 服务类
 * </p>
 *
 * @author Tangpd
 * @since 2020-12-02
 */
public interface SysPermissionsService extends IService<SysPermissionsEntity> {

    ResultJsonBean getAllPermissions();

    ResultJsonBean deletePermissions(SysPermissionsEntity sysPermissionsEntity);

    ResultJsonBean updatePermissions(SysPermissionsEntity sysPermissionsEntity, HttpServletRequest request);

    ResultJsonBean addPermissions(SysPermissionsEntity sysPermissionsEntity, HttpServletRequest request);

    //=============================================================角色-权限关联相关=====================================================================

    ResultJsonBean getUserPermissionsList(SysUserVo sysUserVo);

    ResultJsonBean addRolePermissions(SysRolePermissionsVo sysRolePermissionsVo);

}
