package cn.itcast.test;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.entity.User;
import cn.itcast.mapper.UserMapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

// 这里我们专门来演示一下 QBE， 虽然之前我们已经演示过了，但是看了一下前面的笔记好像不是很全面

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MapperTest1 {
	@Autowired
	private UserMapper userMapper;
	
	// 如何设置查询列表
	// SELECT uid , username FROM mybatis_mapper.t_user 
	@Test
	public void test() throws SQLException {
		Example  example = new Example(User.class);
		example.selectProperties("uid", "username");
		
		List<User> list = userMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			for (User user : list) {
				System.out.println(user);
			}
		}
	}
	
	// 如何添加排序字段
	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user 
	// order by uid ASC,username DESC 
	@Test
	public void test2() {
		Example  example = new Example(User.class);
		example.orderBy("uid").asc().orderBy("username").desc();
		List<User> list = userMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			for (User user : list) {
				System.out.println(user);
			}
		}
	}
	
	// 如何给查询条件分组，如果都是 and 或者 都是 or ，那么我们不需要操心分组
	// 但是如果查询条件中既有 and 又有 or ，那么最好使用分组进行区别，不然非常难以理解 
	//  (username  like '%a%' and uid < 7) or (birthday > 1995-1-1 and gender = '女')
	
	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user
	// WHERE ( ( username like ? and uid < ? ) or ( birthday > ? and gender = ? ) ) 
	@Test
	public void test3() {
		Example  example = new Example(User.class);
		// 我们可以创建两个  criteria 对象，再使用 example 对象把这两个对象拼接在一起
		Criteria criteria1 = example.createCriteria().andLike("username", "%a%")
				                                      .andLessThan("uid", 7);
		Criteria criteria2 = example.createCriteria().andGreaterThan("birthday", "1995-1-1")
		                         						.andEqualTo("gender", '女');
		// 使用example 对象，关联两个条件对象
		example.or(criteria2);
		
		List<User> list = userMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			for (User user : list) {
				System.out.println(user);
			}
		}
	}
	
	// 像这全部是用 and 连接的条件，根本就不需要考虑什么括号 
	//	SELECT uid,username,birthday,gender,address 
	//  FROM mybatis_mapper.t_user 
	//  WHERE ( ( uid between ? and ? and gender = ? and username like ? ) ) 
	@Test
	public void test4() {
		Example  example = new Example(User.class);
		example.createCriteria().andBetween("uid", 1, 10)
								.andEqualTo("gender", '女')
								.andLike("username", "%abc%");
		List<User> list = userMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			for (User user : list) {
				System.out.println(user);
			}
		}
	}
	
	//  像这种全部都是 or 连接的，也不需要考虑什么括号 
	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user
	// WHERE ( ( uid between ? and ? or gender = ? or username like ? ) ) 
	@Test
	public void test5() {
		Example  example = new Example(User.class);
		example.createCriteria().orBetween("uid", 1, 10)
								.orEqualTo("gender", '女')
								.orLike("username", "%abc%");
		List<User> list = userMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			for (User user : list) {
				System.out.println(user);
			}
		}
	}
	
	// 通用Mapper 是没办法进行分组查询的，如果需要分组查询，那需要自己去手写。
	// 除了 count ，好像也没办法进行聚合函数查询
}
