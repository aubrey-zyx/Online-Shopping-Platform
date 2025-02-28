package com.bfs.onlineshopping.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseRequest {
    private List<OrderItemRequest> order;
}
