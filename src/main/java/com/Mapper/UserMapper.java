package com.Mapper;

import com.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper{
    @Select("select * from user")
    List<User> selectUser();
    @Delete("delete from user where id= #{id}")
    void deleteUser(Integer id);
    @Insert("insert into user(username,password) values(#{username},#{password})")
    void insertUser(User u);
    @Select("select * from user where id=#{id}")
    User selectUserById(Integer id);
}
