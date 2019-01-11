package cn.itcast.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.entity.User;
import cn.itcast.mapper.UserMapper2;

// 演示一下自定义的  MybaseMapper 接口，能不能像默认的 Mapper 接口起作用
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MapperTest3 {
	@Autowired
	private UserMapper2 userMapper2;
	
	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user 
	@Test
	public void test() {
		List<User> list = userMapper2.select(new User());
		if(list != null && list.size() > 0) {
			for (User user : list) {
				System.out.println(user);
			}
		}
	}
}
