package com.bfs.onlineshopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WatchlistResponse {
    private Long productId;
    private String productName;
    private double retailPrice;
}
