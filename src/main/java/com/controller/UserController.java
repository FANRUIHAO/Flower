package com.controller;

import com.entity.User;
import com.github.pagehelper.PageInfo;
import com.service.DeptService;
import com.service.UserService;
import com.vo.SexVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @RequestMapping("/login")
    public String login(String username, String password, HttpSession session, Model model) {
        boolean b = userService.check(username, password, session);
        if (b) {
            User user = userService.findByUsername(username); // 获取用户对象
            session.setAttribute("currentUser", user); // 存入 Session
            return "redirect:/shopping/list"; // 登录成功后跳转回商城页面
        }
        model.addAttribute("error", "用户名密码错误");
        return "login";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        // 销毁Session
        session.invalidate();
        return "redirect:/shopping/list"; // 跳转回商城页面
    }
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "/user/register"; // This should match the name of your register.html file
    }


    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "10") int pageSize,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        PageInfo<User> pageInfo = userService.selectUser(keyword, pageNum, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("users", pageInfo.getList());
        return "user/list";
    }


    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id){
        userService.deleteUser(id);

        return "redirect:/user/list";
    }
    @RequestMapping("/add")//添加部门信息
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
        userService.saveUser(u);
        return "redirect:/user/list";
    }
    @RequestMapping("/stat")
    @ResponseBody//不找页面一定要加
    public List<SexVO> stat(){
        return userService.stat();
    }


}















