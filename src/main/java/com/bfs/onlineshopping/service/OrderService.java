package com.bfs.onlineshopping.service;

import com.bfs.onlineshopping.dao.OrderDao;
import com.bfs.onlineshopping.dao.ProductDao;
import com.bfs.onlineshopping.dao.UserDao;
import com.bfs.onlineshopping.domain.*;
import com.bfs.onlineshopping.domain.request.OrderItemRequest;
import com.bfs.onlineshopping.domain.response.ProductDetailResponse;
import com.bfs.onlineshopping.exception.NotEnoughInventoryException;
import com.bfs.onlineshopping.exception.OrderStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final UserDao userDao;

    @Autowired
    public OrderService(OrderDao orderDao, ProductDao productDao, UserDao userDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderDao.getAll();
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUsername(String username) {
        return orderDao.getOrdersByUsername(username);
    }

    @Transactional
    public Order placeOrder(String username, List<OrderItemRequest> orderItemRequests) {
        User user = userDao.loadUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setDatePlaced(LocalDateTime.now());

        List<OrderItem> orderItems = orderItemRequests.stream().map(request -> {
            Product product = productDao.getProductById(request.getProductId());
            if (product.getQuantity() < request.getQuantity()) {
                throw new NotEnoughInventoryException("Not enough stock for product: " + product.getName());
            }
            product.setQuantity(product.getQuantity() - request.getQuantity());
            productDao.update(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(request.getQuantity());
            orderItem.setPurchasedPrice(product.getRetailPrice());
            orderItem.setWholesalePrice(product.getWholesalePrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderDao.add(order);
        return order;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderDao.getOrderById(orderId);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new OrderStatusException("Order does not exist.");
        }
        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new OrderStatusException("Order is already completed and cannot be canceled.");
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() + orderItem.getQuantity());
            productDao.update(product);
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        orderDao.update(order);
        return order;
    }

    @Transactional
    public Order completeOrder(Long orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new OrderStatusException("Order does not exist.");
        }
        if (order.getOrderStatus() != OrderStatus.PROCESSING) {
            throw new OrderStatusException("Only processing orders can be completed.");
        }

        order.setOrderStatus(OrderStatus.COMPLETED);
        orderDao.update(order);
        return order;
    }

    @Transactional(readOnly = true)
    public List<ProductDetailResponse> getMostFrequentlyPurchasedProducts(String username) {
        List<Order> orders = orderDao.getOrdersByUsername(username).stream()
                .filter(order -> order.getOrderStatus() != OrderStatus.CANCELED)
                .collect(Collectors.toList());

        Map<Product, Long> productCount = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProduct, Collectors.counting()));

        return productCount.entrySet().stream()
                .sorted(Map.Entry.<Product, Long>comparingByValue().reversed()
                        .thenComparing(entry -> entry.getKey().getProductId()))
                .limit(3)
                .map(entry -> new ProductDetailResponse(
                        entry.getKey().getProductId(),
                        entry.getKey().getName(),
                        entry.getKey().getDescription(),
                        entry.getKey().getRetailPrice()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDetailResponse> getMostRecentlyPurchasedProducts(String username) {
        return orderDao.getOrdersByUsername(username).stream()
                .filter(order -> order.getOrderStatus() != OrderStatus.CANCELED)
                .sorted(Comparator.comparing(Order::getDatePlaced).reversed())
                .flatMap(order -> order.getOrderItems().stream()
                        .sorted(Comparator.comparing(orderItem -> orderItem.getProduct().getProductId()))
                        .map(orderItem -> new ProductDetailResponse(
                                orderItem.getProduct().getProductId(),
                                orderItem.getProduct().getName(),
                                orderItem.getProduct().getDescription(),
                                orderItem.getProduct().getRetailPrice())))
                .distinct()
                .limit(3)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDetailResponse> getMostProfitableProducts(int limit) {
        return orderDao.getAll().stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED)
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProduct, Collectors.summingDouble(item ->
                        (item.getPurchasedPrice() - item.getWholesalePrice()) * item.getQuantity())))
                .entrySet().stream()
                .sorted(Map.Entry.<Product, Double>comparingByValue().reversed()
                        .thenComparing(entry -> entry.getKey().getProductId()))
                .limit(limit)
                .map(entry -> new ProductDetailResponse(
                        entry.getKey().getProductId(),
                        entry.getKey().getName(),
                        entry.getKey().getDescription(),
                        entry.getKey().getQuantity(),
                        entry.getKey().getRetailPrice(),
                        entry.getKey().getWholesalePrice()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDetailResponse> getMostPopularProducts(int limit) {
        return orderDao.getAll().stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED)
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProduct, Collectors.summingInt(OrderItem::getQuantity)))
                .entrySet().stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed()
                        .thenComparing(entry -> entry.getKey().getProductId()))
                .limit(limit)
                .map(entry -> new ProductDetailResponse(
                        entry.getKey().getProductId(),
                        entry.getKey().getName(),
                        entry.getKey().getDescription(),
                        entry.getKey().getQuantity(),
                        entry.getKey().getRetailPrice(),
                        entry.getKey().getWholesalePrice()))
                .collect(Collectors.toList());
    }
}
