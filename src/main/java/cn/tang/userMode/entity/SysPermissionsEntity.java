package cn.tang.userMode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 权限信息表
 * </p>
 *
 * @author Tangpd
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_permissions")
public class SysPermissionsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "permissions_id", type = IdType.INPUT)
    private String permissionsId;

    /**
     * 权限名称
     */
    private String permissionsName;

    /**
     * 所属层级
     */
    private Integer hierarchy;

    /**
     * 拥有的资源
     */
    private String resourcesList;

    /**
     * 图标
     */
    private String iconUrl;

    /**
     * 跳转路径
     */
    private String hrefUrl;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 创建时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 操作者
     */
    private String updateUser;


}
