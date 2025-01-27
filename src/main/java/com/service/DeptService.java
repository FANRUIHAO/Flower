package com.service;

import com.entity.Dept;
import com.mapper.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptService {
    @Autowired
    private DeptMapper DeptMapper;

    public List<Dept> allDept() {
        return DeptMapper.all();
    }
}
