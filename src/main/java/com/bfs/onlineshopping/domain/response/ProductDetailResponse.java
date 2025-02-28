package com.bfs.onlineshopping.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Hides null fields when they are not needed
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String description;
    private Integer quantity; // Included for admins
    private Double retailPrice;
    private Double wholesalePrice; // Included for admins

    // Constructor for regular users (no quantity and wholesale price)
    public ProductDetailResponse(Long id, String name, String description, Double retailPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.retailPrice = retailPrice;
    }
}
