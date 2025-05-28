package com.service;

import com.entity.Comment;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    public PageInfo<Comment> getComments(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> comments = commentsMapper.getAllComments();
        return new PageInfo<>(comments);
    }

    public void deleteCommentById(Long id) {
        commentsMapper.deleteCommentById(id);
    }
}
