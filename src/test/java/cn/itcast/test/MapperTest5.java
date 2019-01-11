package cn.itcast.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.entity.User;
import cn.itcast.mapper.UserMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MapperTest5 {
	@Autowired
	private UserMapper userMapper;
	
	// 测试一下通用 Mapper 对二级缓存的支持
	// @CacheNamespace
	@Test
	public void test() {
		// 第一次查询
		// 我们调用一次dao 层的方法，方法执行完返回结果以后，其实就已经提交事务了
		User user1 = userMapper.selectByPrimaryKey(10);
		System.out.println(user1);
		
		// 第二次查询
		User user2 = userMapper.selectByPrimaryKey(10);
		System.out.println(user2);
		
		System.out.println(user1 == user2);
	}
}
