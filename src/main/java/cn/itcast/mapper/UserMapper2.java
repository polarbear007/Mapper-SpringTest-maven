package cn.itcast.mapper;

import cn.itcast.baseMapper.MyBaseMapper;
import cn.itcast.baseMapper.MyBatchInsertMapper;
import cn.itcast.entity.User;

public interface UserMapper2 extends MyBaseMapper<User>, MyBatchInsertMapper<User> {

}
