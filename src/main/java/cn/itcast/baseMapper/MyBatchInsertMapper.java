package cn.itcast.baseMapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;

import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface MyBatchInsertMapper<T> {
	@InsertProvider(method="dynamicSQL", type=MyBatchInsertProvider.class)
	public abstract void batchInsert(List<T> list);
}
