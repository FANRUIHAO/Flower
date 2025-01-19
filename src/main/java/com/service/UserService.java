package com.service;

import com.entity.User;
import org.apache.ibatis.jdbc.Null;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    public List<User> selectUser(){
        List<User> list=new ArrayList<>();
        User u=new User();
        u.setId(2);
        u.setUsername("tom");
        u.setPassword("123456");
        list.add(u);
        return list;
    }

}
