package cn.itcast.baseMapper;

import java.util.Set;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class MyBatchInsertProvider extends MapperTemplate{

	// 继承这个 MapperTemplate 抽象类，必须跟父类一样指定一个带参的构造
	public MyBatchInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}
	
//	 <insert id="addBatch">
//		 <foreach collection="list" item="user" >
//	 	 	insert into t_user (username, birthday, gender, address) 
//			values (#{user.username }, #{user.gender }, #{user.address } );
//	 	 </foreach>
// 	 </insert>

	// 我们的这个方法目标就是mybatis 在创建mapper 代理对象的时候，默认是会根据接口全路径名 + 方法名
	//  去 configuration 对象里面找对应的 MappedStatement 对象，而因为我们没有映射文件，
	//  所以通用mapper 就会执行这个方法生成对应的 sql 语句，然后返回给mybatis 
	
	public String batchInsert(MappedStatement ms) {
		// 首先，我们使用父类方法， 获取实体对象的 Class 对象
		Class<?> entityClass = super.getEntityClass(ms);
		// 然后再使用父类方法， 通用这个 Class 对象获取对应的表名
		String tableName = super.tableName(entityClass);
		
		// 然后创建 StringBuilder 对象，用来拼接sql 语句
		StringBuilder sb = new StringBuilder();
		
		sb.append("<foreach collection=\"list\" item=\"user\" >");
		
		// 使用 SqlHelper 拼接里面的 insert 语句   " insert into t_user"
		String insertClause = SqlHelper.insertIntoTable(entityClass, tableName);
		sb.append(insertClause);
		
		// 你要自己来拼接，不使用 SqlHelper 也是没啥问题的。  跟上面的方法作用差不多。 
		// sb.append("insert into ").append(tableName);
		
		// 然后通过 EntityHelper 获取所有的列对象的集合----> 这个列对象里面包含着列名及其对应的参数值
		Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
		// 我们遍历这个集合，可以拿到每个列名和其对应的#{stu.xxx  }  的信息
		StringBuilder insertColums = new StringBuilder();
		StringBuilder insertHolders = new StringBuilder();
		for (EntityColumn entityColumn : columns) {
			// 不管是不是 id 列，我们的插入列表都拼接所有的列，这里就不再搞什么动态 if 判断了
			insertColums.append(entityColumn.getColumn()).append(", ");
			if(entityColumn.isId()) {
				insertHolders.append("null").append(", ");
			}else {
				insertHolders.append(entityColumn.getColumnHolder("user")).append(", ");
			}
		}
		
		// 删除最后多出来的那个 ， 
		String insertColumsStr = insertColums.substring(0, insertColums.lastIndexOf(", "));
		String insertHoldersStr = insertHolders.substring(0, insertHolders.lastIndexOf(", "));
		
		// insert into t_user (username, birthday, gender, address) 
		// values (#{user.username }, #{user.gender }, #{user.address } );
		// 我们的目标大概就是拼接这个
		sb.append("( ").append(insertColumsStr).append(" )");
		sb.append("values ( ").append(insertHoldersStr).append(" );");
		
		// 最后再拼接   </foreach>
		sb.append(" </foreach>");
		
		return sb.toString();
	}

}
