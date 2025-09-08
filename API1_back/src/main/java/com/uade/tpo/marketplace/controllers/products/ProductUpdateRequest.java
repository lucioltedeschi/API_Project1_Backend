
package com.uade.tpo.marketplace.controllers.products;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductUpdateRequest {

    private String name;

    private String description;

    private Float price;

    private Integer stock;

    private Long categoryId;
}
