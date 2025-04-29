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
        // 构建请求体
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Resource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 发送请求到 Flask API
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(FLASK_API_URL, requestEntity, Map.class);

        // 解析响应结果
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && "success".equals(responseBody.get("status"))) {
                String flowerName = (String) responseBody.get("flower_name");

                System.out.println(responseBody.get("flower_name"));

                Product product = shoppingMapper.getProductsByFlowerName(flowerName);
                if (product != null){
                    return product;
                }else{
                    throw new RuntimeException("No product found for the flower name: " + flowerName);
                }
           } else {
                throw new RuntimeException("Flask API returned an error: " + responseBody.get("message"));
            }
        } else {
            throw new RuntimeException("Failed to call Flask API: " + response.getStatusCode());
        }
    } catch (ResourceAccessException e) {
        throw new RuntimeException("Failed to connect to Flask API. Please check if the server is running and accessible.", e);
    } catch (Exception e) {
        throw new RuntimeException("Error processing the image", e);
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
            throw new RuntimeException("No products found for the given keyword: " + keyword);
        }
        return products;
    }


    public List<Notice> getAllNotices() {
        List<Notice> notices = shoppingMapper.getAllNotices();
        if (notices == null || notices.isEmpty()) {
            throw new RuntimeException("No notices found.");
        }
        System.out.println(notices);
        return notices;
    }

    public void saveComplaint(Complaint complaint) {
        // Assuming you have a method in your mapper to save the complaint
        shoppingMapper.saveComplaint(complaint);
    }
    public String getCustomerServicePhone() {
        // Assuming there is a method in the ShoppingMapper to fetch the phone number
        String phoneNumber = shoppingMapper.getCustomerServicePhone();
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new RuntimeException("Customer service phone number not found in the database.");
        }
        return phoneNumber;
    }
}
