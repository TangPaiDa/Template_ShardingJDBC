package cn.tang.userMode.vo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: tangpd
 * @Date: 2020/12/15 11:01
 * @Description:
 */
@Data
public class SysRolePermissionsVo {

    /**
     * 角色主键
     */
    private String roleId;

    /**
     * 权限主键
     */
    private List<String> permissionsIdList;


}
