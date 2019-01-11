package cn.itcast.mapper;

import org.apache.ibatis.annotations.CacheNamespace;

import cn.itcast.entity.User;
import tk.mybatis.mapper.common.Mapper;

@CacheNamespace
public interface UserMapper extends Mapper<User> {

}
