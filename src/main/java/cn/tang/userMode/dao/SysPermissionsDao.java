package cn.tang.userMode.dao;

import cn.tang.userMode.entity.SysPermissionsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限信息表 Mapper 接口
 * </p>
 *
 * @author Tangpd
 * @since 2020-12-02
 */
public interface SysPermissionsDao extends BaseMapper<SysPermissionsEntity> {

    //根据用户 id 获取用户全部权限，拼接为一串字符串返回
    String getUserPermissionsListStrByUserId(String userId);

    //根据用户 id、权限父 id 获取权限信息
    List<Map<String, Object>> getUserPermissionsListByUserIdAndParentId(@Param("userId") String userId, @Param("parentId") String parentId);

    //根据权限父 id 获取权限信息
    List<Map<String, Object>> getUserPermissionsListByParentId(String parentId);
}
