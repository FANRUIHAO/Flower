package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.Mapper")
//把包中所有的接口用过动态代理的方式创建对象
public class flowerApplication {
    public static void main(String[]args){
        SpringApplication.run(flowerApplication.class,args);
    }
}
