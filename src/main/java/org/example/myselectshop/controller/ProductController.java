package org.example.myselectshop.controller;

import lombok.RequiredArgsConstructor;
import org.example.myselectshop.dto.ProductMypriceRequestDto;
import org.example.myselectshop.dto.ProductRequestDto;
import org.example.myselectshop.dto.ProductResponseDto;
import org.example.myselectshop.security.UserDetailsImpl;
import org.example.myselectshop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;


    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto dto) {
        ProductResponseDto product = productService.createProduct(dto);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductMypriceRequestDto dto) {
        ProductResponseDto productResponseDto = productService.updateProduct(id, dto);
        return ResponseEntity.ok(productResponseDto);

    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(productService.getProducts(userDetails.getUser(), page-1, size, sortBy, isAsc));
    }

    @PostMapping("/products/{productId}/folder")
    public void addFolder(@PathVariable Long productId, @RequestParam Long folderId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        productService.addFolder(productId, folderId, userDetails.getUser());
    }


}
