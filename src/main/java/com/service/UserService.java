package com.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public PageInfo<User> selectUser(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectUser(keyword);
        return new PageInfo<>(list);
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
        boolean isPasswordCorrect=u.getPassword().equals(password); //密码是否正确
        if(isPasswordCorrect){
            session.setAttribute("currentUser",u);
        }
        return isPasswordCorrect;
    }
    public List<SexVO> stat() {
        return userMapper.stat();
    }

    public User findByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    public void registerUser(User u) {
        userMapper.registUser(u);
    }

    public void updateUserImage(User user) {
        userMapper.updateUserImage(user);
    }

    public void updateUser(User u) {
        userMapper.updateUser(u);
    }
}






