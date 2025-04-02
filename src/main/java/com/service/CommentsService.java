package com.service;

import com.entity.Comment;
import com.mapper.CommentsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;

    public List<Comment> getCommentsByProductName(String productName) {
        return commentsMapper.getCommentsByProductName(productName);
    }
}
