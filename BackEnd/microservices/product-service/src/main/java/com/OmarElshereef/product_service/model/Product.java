package com.OmarElshereef.product_service.model;



import com.OmarElshereef.product_service.Exceptions.InsufficientStockException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue()
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    private String description;

    @Column(nullable = false)
    private Integer totalStock;

    @Column(nullable = false)
    private Integer availableStock;

    @Column(nullable = false)
    private Integer reservedStock;

    @Column(nullable = false)
    private double price;

    public void reserveStock(Integer quantity) {
        if (availableStock < quantity) {
            throw new InsufficientStockException(
                    "Not enough stock for product " + id +
                            ". Available: " + availableStock + ", Requested: " + quantity
            );
        }

        this.availableStock -= quantity;
        this.reservedStock += quantity;
    }

    public void releaseReservedStock(Integer quantity) {
        this.reservedStock -= quantity;
        this.availableStock += quantity;
    }

    public void confirmReservedStock(Integer quantity) {
        this.reservedStock -= quantity;
        // Stock is now permanently reduced (sold)
    }


}
