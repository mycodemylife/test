package com.open.web.mapper;

import com.open.web.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 获取全部用户
     * @return
     */
    public List<User> getAllUser();

    /**
     * 通过用户名获取密码
     * @param username 用户名
     * @return
     */
    public String getPasswordByUsername(@Param("username") String username);

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @return
     */
    public User getUserByUsername(@Param("username")String username);

    /**
     * 添加用户
     * @param user 用户信息
     * @return
     */
    public void addUser(User user);
}
