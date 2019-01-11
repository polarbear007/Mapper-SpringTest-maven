package cn.itcast.test;

import java.util.ArrayList;
import java.util.Date;
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
	
	// 测试一下自定义的通用批量插入方法好不好使
	// 【注意】 如果我们想要一次性发送多条sql 语句，那么就需要在连接数据库的 url 上添加  allowMutiQueries=true 参数
	// 【注意】 我们其实以前说过，如果要使用批量操作的话，其实一次性发送多条sql 的这种方式并不是太好
	//        而是应该使用Mybatis 的BatchExecutor 来发送一次sql ，然后再批量发送参数
	//        这里只是为了演示一下自定义通用方法如何去写
	
	// INSERT INTO mybatis_mapper.t_user ( uid, username, birthday, gender, address )values ( null, ?, ?, ?, ? ); 
	// INSERT INTO mybatis_mapper.t_user ( uid, username, birthday, gender, address )values ( null, ?, ?, ?, ? ); 
	// INSERT INTO mybatis_mapper.t_user ( uid, username, birthday, gender, address )values ( null, ?, ?, ?, ? ); 
	@Test 
	public void test2() {
		List<User> list = new ArrayList<>();
		User user = new User();
		user.setUsername("Jacky");
		user.setGender('男');
		user.setAddress("广东佛山");
		list.add(user);
		
		User user1 = new User();
		user1.setUsername("Rose");
		user1.setBirthday(new Date());
		user1.setGender('女');
		list.add(user1);
		
		
		User user2 = new User();
		user2.setUsername("Tom");
		user2.setBirthday(new Date());
		user2.setAddress("广东深圳");
		list.add(user2);
		
		userMapper2.batchInsert(list);
	}
}
