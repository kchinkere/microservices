package com.techie.productservice.service;

import com.techie.productservice.dto.ProductRequest;
import com.techie.productservice.dto.ProductResponse;
import com.techie.productservice.model.Product;
import com.techie.productservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        log.info("Saving product....");
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved ", product.getId());
    }


    public List<ProductResponse> getAll() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(this::productResponse).toList();
    }


    public ProductResponse productResponse(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .id(product.getId())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
