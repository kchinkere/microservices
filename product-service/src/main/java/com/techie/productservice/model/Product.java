package com.techie.productservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "product")
@Getter
@Setter
@Builder
public class Product {
    
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;

}
