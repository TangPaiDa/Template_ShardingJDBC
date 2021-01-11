package cn.tang.userMode.vo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: tangpd
 * @Date: 2020/12/14 11:18
 * @Description:
 */
@Data
public class SysUserRoleVo {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色id
     */
    private List<String> roleIdList;

}
