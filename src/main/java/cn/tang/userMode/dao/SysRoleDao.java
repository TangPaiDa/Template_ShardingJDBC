package cn.tang.userMode.dao;

import cn.tang.userMode.entity.SysRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author Tangpd
 * @since 2020-12-03
 */
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

    //获取角色所拥有的权限信息
    List<String> getPermissionsByRoleId(String roleId);

    //获取用户所拥有的角色名称集合
    String getUserRoleListStrByUserId(String userId);

    //获取用户所拥有的角色
    List<SysRoleEntity> getUserRoleListByUserId(String userId);

}
