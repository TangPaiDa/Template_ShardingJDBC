package cn.tang.userMode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色权限表
 * </p>
 *
 * @author Tangpd
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role_permissions")
public class SysRolePermissionsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色权限id
     */
    @TableId(value = "role_permissions_id", type = IdType.INPUT)
    private String rolePermissionsId;

    /**
     * 角色主键
     */
    private String roleId;

    /**
     * 权限主键
     */
    private String permissionsId;


}
