package com.controller;

import com.entity.*;
import com.service.CartService;
import com.service.CommentsService;
import com.service.ShoppingService;
import com.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/")
    public String home() {
        return "redirect:/shopping/list"; // 根路径跳转到购物列表页面
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
        // Check if the product is already in the favorite list
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
        // Remove product from favorite logic
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
        if (productPrice.isNaN() || productPrice.isInfinite()) {
            response.put("status", "error");
            response.put("message", "Invalid product price!");
            return response;
        }
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            response.put("status", "redirect");
            response.put("url", "/user/login");
            return response;
        }
        // Add product to cart logic
        Cart cartItem = new Cart();
        cartItem.setUser_id(user.getId());
        cartItem.setCname(productName);
        cartItem.setCprice(new BigDecimal(productPrice).intValue());
        cartItem.setImage_url(productImage);
        cartItem.setCnum(quantity);
        cartService.save(cartItem);

        response.put("status", "success");
        response.put("message", "Product added to cart successfully!");
        return response;
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

        List<Cart> cartItems = cartService.getCartItemsByUserId(user.getId().longValue());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("showAdminButton", user.getGrade() == User.Grade.ADMIN);
        return "shopping/cart";

    }

    @GetMapping("/removeFromCart")
    public String removeFromCart(@RequestParam Long id, HttpSession session) {
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

        return "shopping/profile"; // 返回个人信息页面
    }

    @PostMapping("/update-avatar")
    public String updateAvatar(@RequestParam("avatar") MultipartFile avatar, HttpSession session, Model model) {
        if (!avatar.isEmpty()) {
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

                User user = (User) session.getAttribute("currentUser");
                if (user == null) {
                    throw new IllegalStateException("User is not found in session.");
                }

                String newAvatarPath = "/images/person/" + fileName + "?t=" + System.currentTimeMillis();
                user.setUser_image(newAvatarPath);
                userService.updateUserImage(user);
                session.setAttribute("currentUser", user);

                session.setAttribute("message", "Avatar updated successfully!");

                model.addAttribute("newAvatarPath", newAvatarPath);

            } catch (IOException e) {
                e.printStackTrace();
                // Handle file upload error
                return "redirect:/shopping/profile?error=upload";
            }
        }
        return "redirect:/shopping/profile"; // If upload fails, redirect to the profile page
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
}