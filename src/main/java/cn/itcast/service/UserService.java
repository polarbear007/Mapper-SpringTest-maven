package cn.itcast.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.mapper.UserMapper;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	
}
