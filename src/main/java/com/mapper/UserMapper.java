package com.mapper;

import com.entity.User;
import com.vo.SexVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper{
    List<User> selectUser(String keyword);
    @Delete("delete from user where id= #{id}")
    void deleteUser(Integer id);
    @Insert("insert into user(username,password,sex,grade,user_image) values(#{username},#{password},#{sex},#{grade},#{user_image})")
    void insertUser(User u);
    @Update("update user set username=#{username}, password=#{password}, sex=#{sex}, grade=#{grade}, user_image=#{user_image} ,phone=#{phone} where id=#{id}")
    void updateUser(User u);
    @Select("select * from user where id=#{id}")
    User selectUserById(Integer id);
    @Select("select * from user where username=#{username}")
    User selectUserByUsername(String username);
    @Select("SELECT sex as 'name',count(id) as 'value' FROM USER GROUP BY sex")
    List<SexVO> stat();
    @Insert("insert into user(username,password,sex,grade,user_image) values(#{username},#{password},#{sex},1,#{user_image})")
    void registUser(User u);
    @Update("update user set user_image=#{user_image} where id=#{id}")
    void updateUserImage(User user);


}













