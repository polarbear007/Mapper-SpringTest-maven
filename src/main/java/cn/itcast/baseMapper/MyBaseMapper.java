package cn.itcast.baseMapper;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ExampleMapper;

public interface MyBaseMapper<T> extends BaseMapper<T>, ExampleMapper<T>{}
