package cn.tang.userMode.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Auther: tangpd
 * @Date: 2020/10/23 13:28
 * @Description:
 */
@Data
public class SysUserVo {

    /**
     * 主键id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String oldPassword;

    private String newPassword;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态:0启用、1禁用、2删除
     */
    private String status;

    /**
     * 身份:0普通用户
     */
    private String type;

    /**
     * 分页长度
     */
    private Integer pageSize;

    /**
     * 分页页码数
     */
    private Integer pageNum;

    /**
     * 模糊查询条件
     */
    private String selectParam;

}
