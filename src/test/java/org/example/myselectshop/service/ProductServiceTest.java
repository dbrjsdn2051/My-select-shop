package org.example.myselectshop.service;

import jakarta.persistence.EntityManager;
import org.example.myselectshop.dto.ProductResponseDto;
import org.example.myselectshop.entity.User;
import org.example.myselectshop.entity.UserRoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;
    @Autowired
    EntityManager em;

    @Test
    void getProducts() {
        User user = new User();
        user.setEmail("aaaaa@naver.com");
        user.setRole(UserRoleEnum.ADMIN);
        user.setUsername("Admin");
        user.setPassword("1234");
        em.persist(user);


        Page<ProductResponseDto> list = productService.getProducts(user, 1, 10, "title", true);
        for (ProductResponseDto productResponseDto : list) {
            System.out.println("productResponseDto = " + productResponseDto.toString());
        }

    }
}