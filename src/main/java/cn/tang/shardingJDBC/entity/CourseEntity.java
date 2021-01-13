package cn.tang.shardingJDBC.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Tangpd
 * @since 2021-01-11
 */
@Data
@TableName("course")
public class CourseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private int cId;

    /**
     * 课程名称
     */
    private String cName;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 状态
     */
    private String cStatus;

}
