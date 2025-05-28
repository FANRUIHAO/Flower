package com.service;

import com.entity.*;
import com.mapper.CartMapper;
import com.mapper.ShoppingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class ShoppingService {
    @Autowired
    private  ShoppingMapper shoppingMapper;
    @Autowired
    private CartMapper cartMapper;
    private static final String FLASK_API_URL = "http://localhost:5000/predict";

    public Product  identifyFlower(MultipartFile file) {
    try {
        // 构建请求体 设置http请求头，声明上传的是文件
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        //使用 ByteArrayResource 将 MultipartFile 转成内存资源，封装到请求体 body 中。

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Resource resource = new ByteArrayResource(file.getBytes()) {
            //让 Flask 能识别文件名。
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("file", resource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 使用 Spring 的 RestTemplate 发送 POST 请求，把图片传给 Flask 接口。
        //返回值是 ResponseEntity<Map>，也就是 Flask 返回的 JSON 被解析为 Java 的 Map。
        RestTemplate restTemplate = new RestTemplate();
        //泛型能在不确定返回值结构时，用通用的 Map<String, Object> 封装数据
        ResponseEntity<Map> response = restTemplate.postForEntity(FLASK_API_URL, requestEntity, Map.class);

        // 解析响应结果
        if (response.getStatusCode() == HttpStatus.OK) {
            //用 Map<String, Object> 来接收这个 JSON响应
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && "success".equals(responseBody.get("status"))) {
                String flowerName = (String) responseBody.get("flower_name");
                System.out.println(responseBody.get("flower_name"));
                Product product = shoppingMapper.getProductsByFlowerName(flowerName);
                if (product != null){
                    return product;
                }else{
                    throw new RuntimeException("未找到: " + flowerName);
                }
           } else {
                throw new RuntimeException("Flask API 错误: " + responseBody.get("message"));
            }
        } else {
            throw new RuntimeException("Flask API错误: " + response.getStatusCode());
        }
    } catch (ResourceAccessException e) {
        throw new RuntimeException("连接 Flask API失败.", e);
    } catch (Exception e) {
        throw new RuntimeException("错误", e);
    }
    }

    public  List<Product> getAllProducts() {
        return shoppingMapper.findAll();
    }

    public List<Product> filterProducts(String category, double price) {
        return shoppingMapper.filterProducts(category, price);
    }

    public void showComment(String comment, String cproduct) {
        shoppingMapper.showComment(comment, cproduct);
    }

    public void addToFavorite(String username, String productName, Integer productPrice, String productImage) {

        shoppingMapper.addToFavorite(username, productName, productPrice, productImage);
    }

    public boolean isProductCollected(String arg1, String productName) {
        return shoppingMapper.isProductCollected(arg1, productName) > 0;
    }

    public void removeFromFavorite(String arg1, String productName) {
        shoppingMapper.removeFromFavorite(arg1, productName);
    }

    public List<Collect> listcollect(String username) {
        return shoppingMapper.listcollect(username);
    }

    public void addOrder(Order order) {
        shoppingMapper.addOrder(order);
    }

    public List<Product> search(String keyword) {
        List<Product> products = shoppingMapper.search(keyword);
        if (products == null || products.isEmpty()) {
            throw new RuntimeException("未查询到结果: " + keyword);
        }
        return products;
    }

    public List<Notice> getAllNotices() {
        List<Notice> notices = shoppingMapper.getAllNotices();
        if (notices == null || notices.isEmpty()) {
            throw new RuntimeException("没有通知.");
        }
        System.out.println(notices);
        return notices;
    }

    public void saveComplaint(Complaint complaint) {
        shoppingMapper.saveComplaint(complaint);
    }

    public String getCustomerServicePhone() {
        String phoneNumber = shoppingMapper.getCustomerServicePhone();
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new RuntimeException("该手机号码不存在.");
        }
        return phoneNumber;
    }

    public void savePaymentRecord(Record paymentRecord) {
        cartMapper.savePaymentRecord(paymentRecord);
    }
}
