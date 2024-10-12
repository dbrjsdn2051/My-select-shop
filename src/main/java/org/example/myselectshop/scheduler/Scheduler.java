package org.example.myselectshop.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.myselectshop.entity.Product;
import org.example.myselectshop.naver.controller.NaverApiController;
import org.example.myselectshop.naver.dto.ItemDto;
import org.example.myselectshop.naver.service.NaverApiService;
import org.example.myselectshop.repository.ProductRepository;
import org.example.myselectshop.service.ProductService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "Scheduler")
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final NaverApiService naverApiService;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Scheduled(cron = "0 0 1 * * *")
    public void updatePrice() throws InterruptedException{
        log.info("가격 업데이터 실행");
        List<Product> productList = productRepository.findAll();
        for (Product product : productList) {
            TimeUnit.SECONDS.sleep(1);;

            String title = product.getTitle();
            List<ItemDto> itemDtoList = naverApiService.searchItems(title);

            if(!itemDtoList.isEmpty()){
                ItemDto itemDto = itemDtoList.get(0);

                Long id = product.getId();
                try {
                    productService.updateBySearch(id, itemDto);
                } catch (Exception e){
                    log.error(id + " : " + e.getMessage());
                }
            }
        }

    }

}
