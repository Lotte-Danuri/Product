package com.lotte.danuri.product.service.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements  KafkaConsumerService{
    private final ProductRepository productRepository;

    public Map<Object, Object> kafkaInit(String kafkaMessage){
        log.info("Kafka Message : => "+ kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try{
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch(JsonProcessingException ex){
            ex.printStackTrace();
        }
        return map;
    }

    @KafkaListener(topics = "order-insert")
    public void updateQty(String kafkaMessage){

        Map<Object, Object> msgInfo = kafkaInit(kafkaMessage);
        Optional<Product> product = productRepository.findById(Long.valueOf(String.valueOf(msgInfo.get("productId"))));

        //TODO : 상품이 존재하지 않을 경우 rollback
        if (product.isEmpty()){

        }

        //TODO : 상품 수량이 부족할 경우 rollback
        if(product.get().getStock() < 1){

        }

        product.get().updateStock(product.get().getStock() - Long.valueOf(String.valueOf(msgInfo.get("productQuantity"))));
        productRepository.save(product.get());
    }

    @KafkaListener(topics = "like-insert")
    public void likeInsertCount(String kafkaMessage){

        Map<Object, Object> msgInfo = kafkaInit(kafkaMessage);
        List<Product> productList = productRepository.findAllByProductCodeAndDeletedDateIsNull(String.valueOf(msgInfo.get("productCode")));
        productList.forEach(v -> {
            v.updateLikeCount(v.getLikeCount()+1);
        });
        productRepository.saveAll(productList);
    }

    @KafkaListener(topics = "like-delete")
    public void likeDeleteCount(String kafkaMessage){

        Map<Object, Object> msgInfo = kafkaInit(kafkaMessage);
        List<Product> productList = productRepository.findAllByProductCodeAndDeletedDateIsNull(String.valueOf(msgInfo.get("productCode")));
        productList.forEach(v -> {
            v.updateLikeCount(v.getLikeCount()-1);
        });
        productRepository.saveAll(productList);
    }
}
