package cn.itcast.mapper;

import cn.itcast.entity.Employees;
import tk.mybatis.mapper.common.Mapper;

public interface EmployeesMapper extends Mapper<Employees> {
	public void save(Employees exmployees);
}