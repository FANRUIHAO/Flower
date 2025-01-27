package com.mapper;

import com.entity.Dept;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DeptMapper  {
    @Select("select * from dept")
    List<Dept> all();
}
