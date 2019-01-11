package cn.itcast.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.entity.User;
import cn.itcast.mapper.UserMapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

// 演示一下通用Mapper 特有的 QBE 查询

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MapperTest2 {
	@Autowired
	private UserMapper userMapper;
	
	// SELECT uid , username , birthday , gender 
	// FROM mybatis_mapper.t_user 
	// WHERE ( ( uid > ? and username like ? ) ) 
	// order by birthday Asc,gender Desc 
	@Test
	public void test() {
		// 如果我们使用这种方式来创建 example 对象的话，那么我们会感觉非常直观，因为跟 sql 语句的结构非常相似
		// 这种 QBE查询，跟JPA提供的那套新的  QBC查询接口非常相似！！！ 我们应该非常好理解 。
		// 【注意】 where 里面添加的条件是    Sqls 对象，这个对象没什么特别的，这个对象其实就是Criteria 对象的包装类
		//        我们可以使用 静态方法   Sqls.custom() 创建一个包含 Criteria 对象的 Sqls 对象，然后添加查询条件。
		Example example = Example.builder(User.class)
		                 .select("uid", "username", "birthday", "gender") // 设置查询列表
		                 .where(Sqls.custom().andGreaterThan("uid", 10)   // 添加查询条件
		                		 			 .andLike("username", "%o%"))
		                 .orderByAsc("birthday").orderByDesc("gender")    // 设置排序列表
		                 .build();                                        // 创建example 对象
		
		List<User> list = userMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			for (User user : list) {
				System.out.println(user);
			}
		}
	}
	
	
	// SELECT uid , username , birthday , gender 
	// FROM mybatis_mapper.t_user 
	// WHERE ( ( uid > ? and username like ? ) or ( gender = ? ) ) 
	@Test
	public void test2() {
		// 如果多个查询条件的连接符号既有 and 又有 or 的话，那么要注意使用括号进行分组
		Example example = Example.builder(User.class)
				             .select("uid", "username", "birthday", "gender") 
				             .where(Sqls.custom().andGreaterThan("uid", 10)   
                		 			 .andLike("username", "%o%"))
				             .orWhere(Sqls.custom().andEqualTo("gender", '女'))
				             .build();
		
		List<User> list = userMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			for (User user : list) {
				System.out.println(user);
			}
		}
	}

}
