package com.service;

import com.mapper.UserMapper;
import com.entity.User;
import com.vo.SexVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public List<User> selectUser(String keyword){
        List<User> list=userMapper.selectUser(keyword);
        return list;
    }
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    public void saveUser(User u) {//add
        if(u.getId()!=null){
            userMapper.updateUser(u);
        }else{
            userMapper.insertUser(u);
        }

    }

    public User selectUserById(Integer id) {
        return userMapper.selectUserById(id);
    }

    public boolean check(String username, String password, HttpSession session) {
        User u=userMapper.selectUserByUsername(username);
        if(u==null){
            return false;
        }


        boolean b=u.getPassword().equals(password); //密码是否正确
        if(b){
            session.setAttribute("current",u);
        }


        return b;
    }


    public List<SexVO> stat() {
        return userMapper.stat();
    }
}
