package com.mapper;

import com.entity.Comment;
import org.apache.ibatis.annotations.Insert;

public interface CommentMapper {
    @Insert("INSERT INTO comment (username, comment, cproduct) " +
            "VALUES ( #{username}, #{comment}, #{cproduct})")
    void addComment(Comment newComment);
}
