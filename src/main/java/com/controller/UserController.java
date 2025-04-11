package com.controller;

import com.entity.User;
import com.github.pagehelper.PageInfo;
import com.service.UserService;
import com.vo.SexVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
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
    public String index(HttpSession session, Model model) {
        User u = (User) session.getAttribute("currentUser");
        if (u != null) {
            model.addAttribute("username", u.getUsername());
            model.addAttribute("userImage", u.getUser_image());
        } else {
            model.addAttribute("username", "游客");
        }
        return "index";
    }
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, @RequestParam String sex,@RequestParam String confirmPassword, Model model) {
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "user/register";
        }

        // Check if username already exists
        if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists");
            return "user/register";
        }

        // Create new user and save to database
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // Assuming password is already encrypted
        newUser.setSex(sex);
        newUser.setUser_image("/images/person/p1.jpg");
        userService.registerUser(newUser);

        // Redirect to login page after successful registration
        return "redirect:/user/login";

    }
    @GetMapping("/register")
    public String showRegisterPage() {
        return "user/register"; // 返回注册页面
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
    @RequestMapping("/add")
    public String add(Model model){
        return "user/add";
    }
    @RequestMapping("/edit")
    public String edit(@RequestParam Integer id,Model model){
        User u=userService.selectUserById(id);
        model.addAttribute("u",u);
        //把查到的那条数据带回到list页面
        return "user/edit";
    }
    @RequestMapping("/update")
    public String update(User u, @RequestParam("image") MultipartFile image, HttpSession session, Model model) {
        if (!image.isEmpty()) {
            try {
                String fileName = image.getOriginalFilename();
                String uploadDir = new File("target/classes/static/images/person/").getAbsolutePath();
                File uploadDirFile = new File(uploadDir);

                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                String filePath = uploadDir + File.separator + fileName;
                File dest = new File(filePath);
                image.transferTo(dest);

                String newImagePath = "/images/person/" + fileName + "?t=" + System.currentTimeMillis();
                u.setUser_image(newImagePath);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/user/list?error=upload";
            }
        } else {
            User existingUser = userService.selectUserById(u.getId());
            u.setUser_image(existingUser.getUser_image());
        }

        userService.updateUser(u);

        // 仅当当前登录用户被更新时才更新会话
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null && currentUser.getId().equals(u.getId())) {
            session.setAttribute("currentUser", u);
        }

        return "redirect:/user/list";
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















