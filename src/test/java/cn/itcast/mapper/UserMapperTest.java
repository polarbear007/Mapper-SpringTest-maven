package cn.itcast.mapper;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.entity.User;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class UserMapperTest {
	@Autowired
	private UserMapper userMapper;
	
	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user WHERE username = ? AND gender = ? 
	@Test
	public void testSelectOne() {
		User user = new User();
		user.setUsername("小李");
		user.setGender('男');
		User user2 = userMapper.selectOne(user);
		System.out.println(user2);
	}
	
	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user WHERE gender = ? 
	@Test
	public void testSelect() {
		User user = new User();
		user.setGender('男');
		List<User> list = userMapper.select(user);
		if(list != null && list.size() > 0) {
			for (User user2 : list) {
				System.out.println(user2);
			}
		}
	}
	
	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user 
	@Test
	public void testSelectAll() {
		List<User> list = userMapper.selectAll();
		if(list != null && list.size() > 0) {
			for (User user2 : list) {
				System.out.println(user2);
			}
		}
	}
	
	// 如果放进一个什么属性值都没有的空 user 对象，就是查询全表
	// 如果属性有值的话，那么就 是根据等值条件去统计个数
	@Test
	public void testSelectCount() {
	   // SELECT COUNT(*) FROM mybatis_mapper.t_user 
		int count = userMapper.selectCount(new User());
		System.out.println(count);
		System.out.println("========================");
		
		// SELECT COUNT(*) FROM mybatis_mapper.t_user WHERE gender = ? 
		User user = new User();
		user.setGender('女');
		int count2 = userMapper.selectCount(user);
		System.out.println(count2);
	}

	// 根据主键去查询记录， 主键属性必须要有值
	// 【注意】 要求我们在实体类上面必须有  @Id 注解，标注哪一个属性是主键
	//        如果我们有@Id 注解，那么参数值可以是一个属性值， 也可以是一个 包含主键值的实体类对象
	//  【注意】 如果我们没有 @Id 属性，那么发送的 Sql 将不是根据 id 来查询，而是根据所有的属性值来查询！！！【一定要写@Id】
	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user WHERE uid = ? 
	@Test
	public void testSelectByPrimaryKey() {
		User user = new User();
		user.setUid(5);
		User user2 = userMapper.selectByPrimaryKey(5);
		System.out.println(user2);
	}
	
	// SELECT CASE WHEN COUNT(uid) > 0 THEN 1 ELSE 0 END AS result 
	// FROM mybatis_mapper.t_user WHERE uid = ? 
	@Test
	public void testExistsWithPrimaryKey() {
		boolean result = userMapper.existsWithPrimaryKey(12);
		System.out.println(result);
	}

	// INSERT INTO mybatis_mapper.t_user ( uid,username,birthday,gender,address ) VALUES( ?,?,?,?,? )
	// 不管属性值是不是null, 都插入
	// 【注意】 如果我们的主键值是自增的，并且实体类的主键上面有设置 @GeneratedValue(), 那么默认可以返回主键值
	@Test
	public void testInsert() {
		User user = new User();
		user.setUsername("Rose");
		user.setBirthday(new Date());
		user.setGender('女');
		user.setAddress("美国洛杉矶");
		userMapper.insert(user);
		
		// 打印一下主键值
		System.out.println(user.getUid());
	}

	// INSERT INTO mybatis_mapper.t_user ( uid,username,gender,address ) VALUES( ?,?,?,? ) 
	// 我们没有添加  birthday 属性，所以插入的列表里没有 birthday
	@Test
	public void testInsertSelective() {
		User user = new User();
		user.setUsername("Jack");
		user.setGender('男');
		user.setAddress("美国纽约");
		userMapper.insertSelective(user);
	}

	// UPDATE mybatis_mapper.t_user 
	// SET uid = uid,username = ?,birthday = ?,gender = ?,address = ? 
	// WHERE uid = ? 
	
	// Parameters: 老王(String), 2019-01-09 15:26:53.152(Timestamp), 男(String), null, 5(Integer)
	// 【注意】 我们的 address 属性值为 Null , 这里同样改成 null 值
	@Test
	public void testUpdateByPrimaryKey() {
		User user = new User();
		user.setUid(5);
		user.setUsername("老王");
		user.setBirthday(new Date());
		user.setGender('男');
		
		userMapper.updateByPrimaryKey(user);
	}

	// UPDATE mybatis_mapper.t_user 
	// SET uid = uid,username = ?,birthday = ?,gender = ? WHERE uid = ? 
	
	// Parameters: 老王(String), 2019-01-09 15:28:50.402(Timestamp), 男(String), 5(Integer)
	// 【注意】  我们的 address 属性值为 Null ， 这里就直接不修改这个属性值
	@Test
	public void testUpdateByPrimaryKeySelective() {
		User user = new User();
		user.setUid(5);
		user.setUsername("老王");
		user.setBirthday(new Date());
		user.setGender('男');
		
		userMapper.updateByPrimaryKeySelective(user);
	}

	// 根据 user 条件的属性值作等值条件，进行删除
	// DELETE FROM mybatis_mapper.t_user WHERE address = ? 
	@Test
	public void testDelete() {
		User user = new User();
		user.setAddress("美国纽约");
		userMapper.delete(user);
	}
	
	// 根据主键值去删除记录
	// DELETE FROM mybatis_mapper.t_user WHERE uid = ? 
	@Test
	public void testDeleteByPrimaryKey() {
		userMapper.deleteByPrimaryKey(12);
	}

	// QBE查询， 跟原生Mybatis 逆向工程生成的那个 example 完全一样
	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user
	// WHERE ( ( uid > ? and username like ? ) ) 
	@Test
	public void testSelectByExample() {
		Example example = new Example(User.class);
		example.createCriteria().andGreaterThan("uid", 1)
								.andLike("username", "%o%");
		List<User> list = userMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			for (User user : list) {
				System.out.println(user);
			}
		}
	}
	
	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user WHERE ( ( uid = ? ) ) 
	@Test
	public void testSelectOneByExample() {
		Example example = new Example(User.class);
		example.createCriteria().andEqualTo("uid", 5);
		
		User user = userMapper.selectOneByExample(example);
		System.out.println(user);
		
	}

	// SELECT COUNT(*) FROM mybatis_mapper.t_user 
	// WHERE ( ( uid between ? and ? ) ) 
	@Test
	public void testSelectCountByExample() {
		Example example = new Example(User.class);
		example.createCriteria().andBetween("uid", 0, 10);
		int count = userMapper.selectCountByExample(example);
		System.out.println(count);
	}

	// DELETE FROM mybatis_mapper.t_user WHERE ( ( uid > ? ) ) 
	@Test
	public void testDeleteByExample() {
		Example example = new Example(User.class);
		example.createCriteria().andGreaterThan("uid", 100);
		
		userMapper.deleteByExample(example);
		
	}

	// 【注意】 如果 user 对象的某个属性为 null， 这个方法不管，直接把这条记录对应的列值设置为 null
	//        更新的条件就来自于 example 对象
	// UPDATE mybatis_mapper.t_user 
	// SET uid = uid,username = ?,birthday = ?,gender = ?,address = ? 
	// WHERE ( ( uid > ? ) ) 
	@Test
	public void testUpdateByExample() {
		Example example = new Example(User.class);
		example.createCriteria().andGreaterThan("uid", 100);
		
		User user = new User();
		user.setUsername("Tom");
		user.setBirthday(new Date());
		userMapper.updateByExample(user, example);
	}

	// UPDATE mybatis_mapper.t_user 
	// SET uid = uid,username = ?,birthday = ? 
	// WHERE ( ( uid > ? ) ) 
	
	// 对于 user 对象为null 的属性，不进行更新
	@Test
	public void testUpdateByExampleSelective() {
		Example example = new Example(User.class);
		example.createCriteria().andGreaterThan("uid", 100);
		
		User user = new User();
		user.setUsername("Tom");
		user.setBirthday(new Date());
		
		userMapper.updateByExampleSelective(user, example);
	}

	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user 
	// WHERE ( ( uid < ? ) ) LIMIT ? 
	// 【注意】 通用mapper 会进行物理分页，不像原生mybatis 那样使用内存分页
	@Test
	public void testSelectByExampleAndRowBounds() {
		Example example = new Example(User.class);
		example.createCriteria().andLessThan("uid", 100);
		
		List<User> list = userMapper.selectByExampleAndRowBounds(example, new RowBounds(0, 10));
		if(list != null && list.size() > 0) {
			for (User user : list) {
				System.out.println(user);
			}
		}
	}

	// SELECT uid,username,birthday,gender,address 
	// FROM mybatis_mapper.t_user WHERE gender = ? LIMIT ?, ? 
	@Test
	public void testSelectByRowBounds() {
		User user = new User();
		user.setGender('男');
		List<User> list = userMapper.selectByRowBounds(user, new RowBounds(2, 3));
		if(list != null && list.size() > 0) {
			for (User user2 : list) {
				System.out.println(user2);
			}
		}
	}

}
