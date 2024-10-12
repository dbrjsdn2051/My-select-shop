package org.example.myselectshop.service;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.example.myselectshop.dto.ProductMypriceRequestDto;
import org.example.myselectshop.dto.ProductRequestDto;
import org.example.myselectshop.dto.ProductResponseDto;
import org.example.myselectshop.entity.*;
import org.example.myselectshop.naver.dto.ItemDto;
import org.example.myselectshop.repository.FolderRepository;
import org.example.myselectshop.repository.ProductFolderRepository;
import org.example.myselectshop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FolderRepository folderRepository;

    private final ProductFolderRepository productFolderRepository;
    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product savedProduct = productRepository.save(new Product(dto));
        return new ProductResponseDto(savedProduct);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto dto) {
        int myprice = dto.getMyprice();
        if (myprice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해주세요.");
        }

        Product findProduct =
                productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 상품을 찾을 수 없습니다."));

        findProduct.update(dto);

        return new ProductResponseDto(findProduct);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        UserRoleEnum userRoleEnum = user.getRole();
        Page<Product> productsList;

        if (userRoleEnum == UserRoleEnum.USER) {
            productsList = productRepository.findAllByUser(user, pageable);
        } else {
            productsList = productRepository.findAll(pageable);
        }

        return productsList.map(ProductResponseDto::new);
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 상품은 존재하지 않습니다."));
        product.updateByItemDto(itemDto);

    }

    public void addFolder(Long productId, Long folderId, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NullPointerException("해당 상품이 존재하지 않습니다."));

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new NullPointerException("해당 폴더가 존재하지 않습니다."));

        if (!product.getUser().getId().equals(user.getId()) || !folder.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다.");
        }

        Optional<ProductFolder> overlapFolder = productFolderRepository.findByProductAndFolder(product, folder);

        if (overlapFolder.isPresent()) {
            throw new IllegalArgumentException("중복된 폴더 입니다.");
        }

        productFolderRepository.save(new ProductFolder(product, folder));

    }
}
