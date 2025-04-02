package com.mapper;

import com.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentsMapper {
    @Select("select * from comment where cproduct = #{productName}")
    List<Comment> getCommentsByProductName(String productName);
}
