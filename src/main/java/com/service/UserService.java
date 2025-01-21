package com.service;

import com.Mapper.UserMapper;
import com.entity.User;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public List<User> selectUser(){
        List<User> list=userMapper.selectUser();
        return list;
    }
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    public void saveUser(User u) {
        userMapper.insertUser(u);
    }

    public User selectUserById(Integer id) {
        return userMapper.selectUserById(id);
    }

}
