package cn.tang.shardingJDBC.dao;

import cn.tang.shardingJDBC.entity.CourseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Tangpd
 * @since 2021-01-11
 */
public interface CourseDao extends BaseMapper<CourseEntity> {

    @Select("select * from course")
    List<CourseEntity> getList();

}
