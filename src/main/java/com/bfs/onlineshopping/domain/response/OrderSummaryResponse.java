package com.bfs.onlineshopping.domain.response;

import com.bfs.onlineshopping.domain.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Hides null fields in JSON response
public class OrderSummaryResponse {
    private Long id;
    private LocalDateTime datePlaced;
    private OrderStatus orderStatus;
    private String username; // Only included for admin

    // Constructor for regular users (no username)
    public OrderSummaryResponse(Long id, LocalDateTime datePlaced, OrderStatus orderStatus) {
        this.id = id;
        this.datePlaced = datePlaced;
        this.orderStatus = orderStatus;
    }
}
