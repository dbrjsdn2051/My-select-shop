package org.example.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.myselectshop.dto.ProductMypriceRequestDto;
import org.example.myselectshop.dto.ProductRequestDto;
import org.example.myselectshop.naver.dto.ItemDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String images;
    private String link;
    private int lprice;
    private int myprice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product")
    private List<ProductFolder> productFolderList = new ArrayList<>();

    public Product(ProductRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.images = requestDto.getImage();
        this.link = requestDto.getLink();
        this.lprice = requestDto.getLprice();
    }

    public void update(ProductMypriceRequestDto dto) {
        this.myprice = dto.getMyprice();
    }

    public void updateByItemDto(ItemDto itemDto) {
        this.lprice = itemDto.getLprice();
    }
}
