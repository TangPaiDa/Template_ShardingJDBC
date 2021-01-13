package cn.tang;

import cn.tang.shardingJDBC.dao.CourseDao;
import cn.tang.shardingJDBC.entity.CourseEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplateForProjectApplicationTests {

	@Resource
	private CourseDao courseDao;

	@Test
	public void addFor() {
		CourseEntity courseEntity = new CourseEntity();
		for (int i = 1; i < 20; i++) {
			courseEntity.setCName("那个谁" + i);
			courseEntity.setUserId(1000l);
			courseEntity.setCStatus("line");
			courseEntity.setCId(i);
			courseDao.insert(courseEntity);
		}
	}

	@Test
	public void add() {
		CourseEntity courseEntity = new CourseEntity();
		courseEntity.setCName("那个谁" );
		courseEntity.setUserId(1000l);
		courseEntity.setCStatus("line");
		courseDao.insert(courseEntity);
	}

	@Test
	public void getOne() {
		CourseEntity courseEntity = courseDao.selectOne(new QueryWrapper<CourseEntity>().eq("c_id", 1));
		System.out.println(courseEntity);
	}

	@Test
	public void getList() {
		System.out.println(courseDao.getList());
	}


}

