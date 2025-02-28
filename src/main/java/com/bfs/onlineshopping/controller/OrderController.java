package com.bfs.onlineshopping.controller;

import com.bfs.onlineshopping.domain.Order;
import com.bfs.onlineshopping.domain.request.OrderItemRequest;
import com.bfs.onlineshopping.domain.request.PurchaseRequest;
import com.bfs.onlineshopping.domain.response.OrderDetailResponse;
import com.bfs.onlineshopping.domain.response.OrderItemResponse;
import com.bfs.onlineshopping.domain.response.OrderSummaryResponse;
import com.bfs.onlineshopping.exception.OrderAccessException;
import com.bfs.onlineshopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderSummaryResponse>> getAllOrders(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ADMIN"));

        List<Order> orders = isAdmin ? orderService.getAllOrders() : orderService.getOrdersByUsername(authentication.getName());

        List<OrderSummaryResponse> responses = orders.stream().map(order ->
                isAdmin
                        ? new OrderSummaryResponse(order.getOrderId(), order.getDatePlaced(), order.getOrderStatus(), order.getUser().getUsername())
                        : new OrderSummaryResponse(order.getOrderId(), order.getDatePlaced(), order.getOrderStatus())
        ).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }


    @PostMapping
    public ResponseEntity<OrderSummaryResponse> placeOrder(Authentication authentication, @RequestBody PurchaseRequest request) {
        String username = authentication.getName();
        List<OrderItemRequest> orderItems = request.getOrder();
        Order order = orderService.placeOrder(username, orderItems);
        return ResponseEntity.ok(new OrderSummaryResponse(order.getOrderId(), order.getDatePlaced(), order.getOrderStatus()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderById(Authentication authentication, @PathVariable Long orderId) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ADMIN"));

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        if (!isAdmin && !order.getUser().getUsername().equals(authentication.getName())) {
            throw new OrderAccessException("You are not authorized to view this order.");
        }

        OrderDetailResponse response = isAdmin
                ? new OrderDetailResponse(order.getDatePlaced(), order.getOrderStatus(), order.getUser().getUsername(),
                OrderItemResponse.fromOrderItems(order.getOrderItems()))
                : new OrderDetailResponse(order.getDatePlaced(), order.getOrderStatus(),
                OrderItemResponse.fromOrderItems(order.getOrderItems()));

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderSummaryResponse> cancelOrder(@PathVariable Long orderId) {
        Order order = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(new OrderSummaryResponse(order.getOrderId(), order.getDatePlaced(), order.getOrderStatus()));
    }

    @PatchMapping("/{orderId}/complete")
    public ResponseEntity<OrderSummaryResponse> completeOrder(@PathVariable Long orderId) {
        Order order = orderService.completeOrder(orderId);
        return ResponseEntity.ok(new OrderSummaryResponse(order.getOrderId(), order.getDatePlaced(), order.getOrderStatus()));
    }
}
