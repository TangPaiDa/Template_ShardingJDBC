package cn.tang.userMode.service;

import cn.tang.common.utils.ResultJsonBean;
import cn.tang.userMode.entity.SysRoleEntity;
import cn.tang.userMode.vo.SysUserRoleVo;
import cn.tang.userMode.vo.SysUserVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author Tangpd
 * @since 2020-12-03
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    ResultJsonBean getAllRole();

    ResultJsonBean deleteRole(SysRoleEntity sysRoleEntity);

    ResultJsonBean updateRole(SysRoleEntity sysRoleEntity);

    ResultJsonBean addRole(SysRoleEntity sysRoleEntity);

    //=============================================================用户-角色关联相关=====================================================================

    ResultJsonBean getUserRoleList(SysUserVo sysUserVo);

    ResultJsonBean addUserRole(SysUserRoleVo userRoleVo);

    //=============================================================角色-权限关联相关=====================================================================

    ResultJsonBean getPermissionsByRoleId(SysRoleEntity sysRoleEntity);


}
