package org.example.myselectshop.repository;

import org.example.myselectshop.entity.Product;
import org.example.myselectshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


    Page<Product> findAllByUser(User user, Pageable pageable);
}
