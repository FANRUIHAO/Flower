package com.controller;

import com.entity.User;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.service.DeptService;
import com.service.UserService;
import com.vo.SexVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;

    @RequestMapping("/login")
    public String login(String username, String password, HttpSession session){
        boolean b=userService.check(username,password, session);
        if(b){
            return "redirect:/";
        }
        System.out.println("用户名密码错误");
        return "login";
    }
    @RequestMapping("/list")
    public String  list(String keyword,Model model){
        List<User> list=userService.selectUser(keyword);
        model.addAttribute("users",list);

        return "user/list";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id){
        userService.deleteUser(id);

        return "redirect:/user/list";
    }
    @RequestMapping("/add")
    public String add(Model model){

        model.addAttribute("depts",deptService.allDept());

        return "user/add";
    }
    @RequestMapping("/edit")
    public String edit(@RequestParam Integer id,Model model){
        User u=userService.selectUserById(id);
        model.addAttribute("u",u);
        //把查到的那条数据带回到list页面
        return "user/edit";
    }

    @RequestMapping("/save")//添加
    public String save(User u){
        System.out.println("1");
        userService.saveUser(u);
        return "redirect:/user/list";
    }
    @RequestMapping("/stat")
    @ResponseBody//不找页面一定要加
    public List<SexVO> stat(){
        return userService.stat();
    }


}
