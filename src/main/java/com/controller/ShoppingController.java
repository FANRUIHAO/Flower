package com.controller;

import com.entity.*;
import com.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shopping")
public class ShoppingController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingService shoppingService;
    @Autowired
    private CommentsService commentService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String home() {
        return "redirect:/product/list"; // 根路径跳转到购物列表页面
    }
    @PostMapping("/identifyFlower")
    @ResponseBody
    public Product identifyFlower(@RequestParam("image") MultipartFile file) {
        try {
            System.out.println("Uploaded file name: " + file.getOriginalFilename());
            return shoppingService.identifyFlower(file);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/filter")
    @ResponseBody
    public List<Product> filterProducts(@RequestParam String category, @RequestParam double price) {
        return shoppingService.filterProducts(category, price);
    }
    @GetMapping("/filterProducts")
    @ResponseBody
    public List<Product> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double maxPrice) {

        // 获取所有商品
        List<Product> allProducts = shoppingService.getAllProducts();

        // 应用筛选条件
        return allProducts.stream()
                .filter(product -> category == null || category.isEmpty() || product.getCategory().equals(category))
                .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    @GetMapping("/getAllProducts")
    @ResponseBody
    public List<Product> getAllProducts() {
        return shoppingService.getAllProducts();
    }
    @GetMapping("/comments")
    @ResponseBody
    public List<Comment> getProductComments(@RequestParam String productName) {
        return commentService.getCommentsByProductName(productName);
    }
    @GetMapping("/checkCollect")
    @ResponseBody
    public Map<String, Object> checkCollect(@RequestParam String productName, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            response.put("status", "redirect");
            response.put("url", "/user/login");
            return response;
        }
        boolean isCollected = shoppingService.isProductCollected(user.getUsername(), productName);
        response.put("status", "success");
        response.put("isCollected", isCollected);
        return response;
    }
    @PostMapping("/addToFavorite")
    @ResponseBody
    public Map<String, Object> addToFavorite(@RequestParam String productName, HttpSession session,
                                             @RequestParam Integer productPrice,
                                             @RequestParam String productImage) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            response.put("status", "redirect");
            response.put("url", "/user/login");
            return response;
        }
        shoppingService.addToFavorite(user.getUsername(), productName, productPrice, productImage);
        response.put("status", "success");
        response.put("message", "Product added to favorite successfully!");
        return response;
    }
    @PostMapping("removeFromFavorite")
    @ResponseBody
    public Map<String, Object> removeFromFavorite(@RequestParam String productName, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            response.put("status", "redirect");
            response.put("url", "/user/login");
            return response;
        }
        shoppingService.removeFromFavorite(user.getUsername(), productName);

        response.put("status", "success");
        response.put("message", "Product removed from favorite successfully!");
        return response;
    }
    @PostMapping("/addToCart")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestParam String productName,
                                         @RequestParam Double productPrice,
                                         @RequestParam String productImage,
                                         @RequestParam Integer quantity,
                                         HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        // 验证价格
        if (productPrice.isNaN() || productPrice.isInfinite() || productPrice <= 0) {
            response.put("status", "error");
            response.put("message", "Invalid product price!");
            return response;
        }
        // 验证数量
        if (quantity <= 0) {
            response.put("status", "error");
            response.put("message", "Quantity must be greater than 0!");
            return response;
        }
        // 检查用户登录状态
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            response.put("status", "redirect");
            response.put("url", "/user/login");
            return response;
        }
        try {
            // 检查购物车中是否已有该商品
            Cart existingItem = cartService.findByUserIdAndProductName(user.getId(), productName);

            if (existingItem != null) {
                // 如果已存在，更新数量
                int newQuantity = existingItem.getCnum() + quantity;
                existingItem.setCnum(newQuantity);
                cartService.update(existingItem);
                response.put("message", "Product quantity updated in cart!");
            } else {
                // 如果不存在，创建新条目
                Cart cartItem = new Cart();
                cartItem.setUser_id(user.getId());
                cartItem.setCname(productName);
                cartItem.setCprice(productPrice);
                cartItem.setImage_url(productImage);
                cartItem.setCnum(quantity);
                cartService.save(cartItem);
                response.put("message", "Product added to cart successfully!");
            }
            // 获取更新后的购物车商品总数
            int cartItemCount = cartService.getCartItemCount(user.getId());
            response.put("status", "success");
            response.put("cartItemCount", cartItemCount);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to update cart: " + e.getMessage());
        }
        return response;
    }
    @PostMapping("/removeMultipleFromCart")
    @ResponseBody
    public Map<String, Object> removeMultipleFromCart(@RequestParam("ids") List<Long> itemIds, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取用户ID
            Integer userId = (Integer) session.getAttribute("userId");
            // 批量删除购物车商品
            cartService.removeMultipleItems(userId, itemIds);
            result.put("status", "success");
            return result;
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            return result;
        }
    }
    @PostMapping("/updateCartItemQuantity")
    @ResponseBody
    public ResponseEntity<Response> updateCartItemQuantity(@RequestParam Integer id, @RequestParam Integer quantity) {
        try {
            boolean success = cartService.updateCartItemQuantity(id, quantity);
            if(success) {
                return ResponseEntity.ok(new Response("success", "Item quantity updated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new Response("error", "Failed to update quantity"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", "Error occurred: " + e.getMessage()));
        }
    }
    @GetMapping("/collect")
    public String showCollectPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userImage", user.getUser_image());
            model.addAttribute("showAdminButton", user.getGrade() == User.Grade.ADMIN);

        } else {
            model.addAttribute("username", "游客");
            model.addAttribute("showAdminButton", false);
            return "redirect:/user/login";
        }
        List<Collect> favoriteProducts = shoppingService.listcollect(user.getUsername());
        model.addAttribute("favoriteProducts", favoriteProducts);
        return "shopping/collect";
    }
    @RequestMapping("/order")
    public String order(HttpSession session, Model model) {
        // 从 Session 中获取当前用户
        User u = (User) session.getAttribute("currentUser");
        if (u != null) {
            // 如果用户已登录，显示用户信息
            model.addAttribute("username", u.getUsername());
            // 根据用户等级判断是否显示后台管理按钮
            if (u.getGrade() == User.Grade.ADMIN) {
                model.addAttribute("showAdminButton", true);
            } else {
                model.addAttribute("showAdminButton", false);
            }
        } else {
            // 如果未登录，显示游客欢迎信息
            model.addAttribute("username", "游客");
            model.addAttribute("showAdminButton", false);
        }

        return "shopping/order"; // 返回订单页面
    }
    @GetMapping("/list")
    public String shoppingList(HttpSession session, Model model, @RequestParam(value = "user_image", required = false, defaultValue = "/static/images/person/p1.jpg") String user_image) {
        // 从 Session 中获取当前用户
        User u = (User) session.getAttribute("currentUser");
        if (u != null) {
            // 如果用户已登录，显示用户信息
            model.addAttribute("username", u.getUsername());
            model.addAttribute("userImage", u.getUser_image());
            // 根据用户等级判断是否显示后台管理按钮
            if (u.getGrade() == User.Grade.ADMIN) {
                model.addAttribute("showAdminButton", true);
            } else {
                model.addAttribute("showAdminButton", false);
            }
        } else {
            // 如果未登录，显示游客欢迎信息
            model.addAttribute("username", "游客");
            model.addAttribute("showAdminButton", false);
        }
        return "shopping/list"; // 返回商城页面
    }
    @RequestMapping("/shop")
    public String shop(HttpSession session, Model model, @RequestParam(value = "user_image", required = false, defaultValue = "/static/images/default-avatar.png") String user_image) {
        // 从 Session 中获取当前用户
        User u = (User) session.getAttribute("currentUser");
        if (u != null) {
            // 如果用户已登录，显示用户信息
            model.addAttribute("username", u.getUsername());
            model.addAttribute("userImage", u.getUser_image());
            // 根据用户等级判断是否显示后台管理按钮
            if (u.getGrade() == User.Grade.ADMIN) {
                model.addAttribute("showAdminButton", true);
            } else {
                model.addAttribute("showAdminButton", false);
            }
        } else {
            // 如果未登录，显示游客欢迎信息
            model.addAttribute("username", "游客");
            model.addAttribute("showAdminButton", false);
        }
        List<Product> products = shoppingService.getAllProducts();
        model.addAttribute("products", products);
        return "shopping/shop"; // 返回购物页面
    }
    @RequestMapping("/collect")
    public String collect(HttpSession session, Model model) {
        // 从 Session 中获取当前用户
        User u = (User) session.getAttribute("currentUser");
        if (u != null) {
            // 如果用户已登录，显示用户信息
            model.addAttribute("username", u.getUsername());
            // 根据用户等级判断是否显示后台管理按钮
            if (u.getGrade() == User.Grade.ADMIN) {
                model.addAttribute("showAdminButton", true);
            } else {
                model.addAttribute("showAdminButton", false);
            }
        } else {
            // 如果未登录，显示游客欢迎信息
            model.addAttribute("username", "游客");
            model.addAttribute("showAdminButton", false);
        }
        List<Product> products = shoppingService.getAllProducts();
        model.addAttribute("products", products);
        return "shopping/collect"; // 返回收藏页面
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/user/login";
        }
        List<Cart> cartItems = cartService.getCartItemsByUserId(user.getId());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("showAdminButton", user.getGrade() == User.Grade.ADMIN);
        return "shopping/cart";
    }

    @GetMapping("/removeFromCart")
    public String removeFromCart(@RequestParam Integer id, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        cartService.deleteCartItem(id);
        return "redirect:/shopping/cart";
    }
    @RequestMapping("/profile")
    public String profile(HttpSession session, Model model) {
        // 从 Session 中获取当前用户
        User u = (User) session.getAttribute("currentUser");
        if (u != null) {
            // 如果用户已登录，显示用户信息
            model.addAttribute("username", u.getUsername());
            model.addAttribute("userImage", u.getUser_image());
            // 根据用户等级判断是否显示后台管理按钮
            if (u.getGrade() == User.Grade.ADMIN) {
                model.addAttribute("showAdminButton", true);
            } else {
                model.addAttribute("showAdminButton", false);
            }
        } else {
            // 如果未登录，显示游客欢迎信息
            model.addAttribute("username", "游客");
            model.addAttribute("showAdminButton", false);
        }
        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");  // 防止页面刷新重复显示
        }
        return "shopping/profile"; // 返回个人信息页面
    }
    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            @RequestParam(value = "currentPassword", required = false) String currentPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("gender") String gender,
            HttpSession session) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            throw new IllegalStateException("User is not found in session.");
        }

        boolean changesMade = false;
        String message = "";

        // 1. 处理头像上传
        if (avatar != null && !avatar.isEmpty()) {
            try {
                String fileName = avatar.getOriginalFilename();
                String uploadDir = new File("target/classes/static/images/person/").getAbsolutePath();
                File uploadDirFile = new File(uploadDir);

                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                String filePath = uploadDir + File.separator + fileName;
                File dest = new File(filePath);
                avatar.transferTo(dest);

                String newAvatarPath = "/images/person/" + fileName + "?t=" + System.currentTimeMillis();
                user.setUser_image(newAvatarPath);
                changesMade = true;
                message += "头像更新成功！";
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/shopping/profile?error=upload";
            }
        }

        // 2. 处理密码修改
        if (currentPassword != null && !currentPassword.isEmpty() &&
                newPassword != null && !newPassword.isEmpty()) {

            // 验证当前密码是否正确
            if (!userService.verifyPassword(user.getUsername(), currentPassword)) {
                session.setAttribute("message", "当前密码不正确！");
                return "redirect:/shopping/profile";
            }

            // 更新密码
            System.out.println(newPassword);
            user.setPassword(newPassword);
            userService.updatePassword(user);
            changesMade = true;
            message += " 密码更新成功！";
        }

        // 3. 更新其他信息
        if (!phone.equals(user.getPhone())) {
            user.setPhone(phone);
            changesMade = true;
            message += " 手机号更新成功！";
        }

        if (!Objects.equals(address, user.getAddr())) {
            user.setAddr(address);
            changesMade = true;
            message += " 地址更新成功！";

        }


        if (!gender.equals(user.getSex())) {
            user.setSex(gender);
            changesMade = true;
            message += " 性别更新成功！";
        }

        // 如果有任何更改，更新用户信息
        if (changesMade) {
            userService.updateUser(user);
            session.setAttribute("currentUser", user);
            session.setAttribute("message", message.trim());
        } else {
            session.setAttribute("message", "未检测到任何更改");
        }

        return "redirect:/shopping/profile";
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public Map<String, String> uploadImage(@RequestParam("uploadImage") MultipartFile uploadImage, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        if (!uploadImage.isEmpty()) {
            try {
                String fileName = uploadImage.getOriginalFilename();
                String uploadDir = new File("target/classes/static/images/person/").getAbsolutePath();
                File uploadDirFile = new File(uploadDir);

                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                String filePath = uploadDir + File.separator + fileName;
                File dest = new File(filePath);
                uploadImage.transferTo(dest);

                User user = (User) session.getAttribute("currentUser");
                if (user == null) {
                    throw new IllegalStateException("User is not found in session.");
                }

                String newAvatarPath = "/images/person/" + fileName + "?t=" + System.currentTimeMillis();
                user.setUser_image(newAvatarPath);
                userService.updateUserImage(user);
                session.setAttribute("currentUser", user);

                response.put("avatarUrl", newAvatarPath);
            } catch (IOException e) {
                e.printStackTrace();
                response.put("error", "Avatar upload failed.");
            }
        } else {
            response.put("error", "No avatar uploaded.");
        }
        return response;
    }
    @GetMapping("/research")
    @ResponseBody
    public List<Product> rsearch(@RequestParam String keyword) {
        System.out.println("Searching for: " + keyword); // 添加参数值打印
        System.out.println("Request received at /shopping/research");
        List<Product> results = shoppingService.search(keyword);
        System.out.println("Found " + results.size() + " products");
        return results;
    }
    @GetMapping("/getUserAccount")
    @ResponseBody
    public Map<String, Object> getUserBalance(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.put("status", "redirect");
            response.put("url", "/user/login");
        } else {
            response.put("status", "success");
            response.put("account", user.getAccount()); // 假设User类有balance字段
        }

        return response;
    }
    //下单对应的代码
    @PostMapping("/checkout")
    @ResponseBody
    public Map<String, Object> checkout(
            @RequestBody Map<String, Object> requestData,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.put("status", "redirect");
            response.put("url", "/user/login");
            return response;
        }
        try {
            List<Integer> itemIds = (List<Integer>) requestData.get("itemIds");
            String password = (String) requestData.get("password");

            if (!user.getPassword().equals(password)) {
                response.put("status", "error");
                response.put("message", "Incorrect password");
                return response;
            }

            List<Cart> selectedItems = cartService.getSelectedCartItems(user.getId(), itemIds);
            double totalAmount = selectedItems.stream()
                    .mapToDouble(item -> item.getCprice() * item.getCnum())
                    .sum();

            if (user.getAccount() < totalAmount) {
                response.put("status", "error");
                response.put("message", "Insufficient account");
                return response;
            }

            // 创建订单（一个商品一个订单）
            List<Order> orders = orderService.createOrder(user, selectedItems, user.getAddr());

            // 扣款并更新账户
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Record paymentRecord = new Record();
            paymentRecord.setUserId(user.getId());
            paymentRecord.setPrice(totalAmount);
            paymentRecord.setTime(sdf.format(new Date())); // 使用格式化字符串
            shoppingService.savePaymentRecord(paymentRecord);

            response.put("status", "success");
            response.put("orderCount", orders.size());
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }
    @GetMapping("/notice")
    @ResponseBody
    public List<Notice> notice() {
        List<Notice> notices = shoppingService.getAllNotices();
        return notices;
    }
    @PostMapping("/complaint")
    @ResponseBody
    public ResponseEntity<String> saveComplaint(@RequestBody Complaint complaint) {
        shoppingService.saveComplaint(complaint);
        return ResponseEntity.ok("投诉提交成功");
    }

    @GetMapping("/contact")
    @ResponseBody
    public String getServicePhone() {
        String phoneNumber = shoppingService.getCustomerServicePhone();
        return phoneNumber != null ? phoneNumber : "电话号码发生错误";
    }
}