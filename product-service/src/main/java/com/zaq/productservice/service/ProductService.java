package com.zaq.productservice.service;

import com.zaq.productservice.dto.ProductRequest;
import com.zaq.productservice.dto.ProductResponse;
import com.zaq.productservice.model.Product;
import com.zaq.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .name(productRequest.getName())
                .build();

        productRepository.save(product);
        log.info("product {} save",product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
