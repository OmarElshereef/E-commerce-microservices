package com.OmarElshereef.product_service.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    private String name;

    private String category;

    private String description;

    private double price;
}
