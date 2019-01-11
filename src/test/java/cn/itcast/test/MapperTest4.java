package cn.itcast.test;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.entity.Employees;
import cn.itcast.entity.GenderType;
import cn.itcast.mapper.EmployeesMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MapperTest4 {
	@Autowired
	private EmployeesMapper employeesMapper;
	
	@Test
	public void test() {
		List<Employees> list = employeesMapper.selectByRowBounds(new Employees(), new RowBounds(0, 10));
		if(list != null && list.size() > 0) {
			for (Employees employees : list) {
				System.out.println(employees);
			}
		}
	}
	
	// 使用自己手动添加的接口方法添加数据
	//  对比下面使用通用Mapper 提供的方法添加数据时， GenderType 会有什么区别
	@Test
	public void test2() {
		Employees employees = new Employees();
		employees.setFirstName("小明");
		employees.setLastName("周");
		employees.setBirthDate(new Date());
		employees.setGender(GenderType.M);
		employees.setHireDate(new Date());
		employeesMapper.save(employees);
		
		System.out.println(employees);
	}
	
	// 
	@Test
	public void test3() {
		Employees employees = new Employees();
		employees.setFirstName("小明");
		employees.setLastName("周");
		employees.setBirthDate(new Date());
		employees.setGender(GenderType.M);
		employees.setHireDate(new Date());
		
		employeesMapper.insert(employees);
		
		System.out.println(employees);
	}
	
	// 如果 实体类上的  gender 属性  没有添加 @Column 的话，那么对于枚举或者复杂类型的属性
	// 通用mapper 的方法都会忽略这个属性，不管是在设置参数还是在封装数据的时候都忽略 
	// 如果我们添加了 @Column 的话，那么通用mapper 就会试图使用现成的 typeHandler 进行处理
	//  如果实在找不到对应的 typehandler ，那么就会
	@Test
	public void test4() {
		Employees employees = employeesMapper.selectByPrimaryKey(10002);
		System.out.println(employees);
	}
}
