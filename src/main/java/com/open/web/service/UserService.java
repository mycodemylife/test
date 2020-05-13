package com.open.web.service;

import com.open.web.bean.User;
import com.open.web.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取全部用户
     * @return
     */
    public List<User> getAllUser(){
        return userMapper.getAllUser();
    }

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @return
     */
    public User getUserByUsername(String username){
        return userMapper.getUserByUsername(username);
    }

    /**
     * 添加用户
     * @param user 用户信息
     */
    public void addUser(User user){
        userMapper.addUser(user);
    }
}
