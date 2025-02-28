package com.bfs.onlineshopping.controller;

import com.bfs.onlineshopping.domain.Product;
import com.bfs.onlineshopping.domain.response.ProductDetailResponse;
import com.bfs.onlineshopping.service.OrderService;
import com.bfs.onlineshopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public ProductController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDetailResponse>> getAllProducts(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ADMIN"));

        List<Product> products = isAdmin ? productService.getAllProducts() : productService.getAvailableProducts();
        List<ProductDetailResponse> responses = products.stream()
                .map(product -> isAdmin ?
                        new ProductDetailResponse(
                                product.getProductId(),
                                product.getName(),
                                product.getDescription(),
                                product.getQuantity(),
                                product.getRetailPrice(),
                                product.getWholesalePrice()) :
                        new ProductDetailResponse(
                                product.getProductId(),
                                product.getName(),
                                product.getDescription(),
                                product.getRetailPrice()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(Authentication authentication, @PathVariable Long id) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ADMIN"));

        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        ProductDetailResponse response = isAdmin ?
                new ProductDetailResponse(
                        product.getProductId(),
                        product.getName(),
                        product.getDescription(),
                        product.getQuantity(),
                        product.getRetailPrice(),
                        product.getWholesalePrice()) :
                new ProductDetailResponse(
                        product.getProductId(),
                        product.getName(),
                        product.getDescription(),
                        product.getRetailPrice());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/frequent/3")
    public ResponseEntity<List<ProductDetailResponse>> getMostFrequentlyPurchasedProducts(Authentication authentication) {
        String username = authentication.getName();
        List<ProductDetailResponse> products = orderService.getMostFrequentlyPurchasedProducts(username);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/recent/3")
    public ResponseEntity<List<ProductDetailResponse>> getMostRecentlyPurchasedProducts(Authentication authentication) {
        String username = authentication.getName();
        List<ProductDetailResponse> products = orderService.getMostRecentlyPurchasedProducts(username);
        return ResponseEntity.ok(products);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateProduct(Authentication authentication, @PathVariable Long id, @RequestBody Product updatedProduct) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).body("Only admin can update products.");
        }

        boolean updated = productService.updateProduct(id, updatedProduct);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Product updated successfully.");
    }

    @PostMapping
    public ResponseEntity<String> createProduct(Authentication authentication, @RequestBody Product newProduct) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).body("Only admin can create products.");
        }

        productService.createProduct(newProduct);
        return ResponseEntity.ok("Product created successfully.");
    }

    @GetMapping("/profit/{limit}")
    public ResponseEntity<List<ProductDetailResponse>> getMostProfitableProducts(Authentication authentication, @PathVariable int limit) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        List<ProductDetailResponse> products = orderService.getMostProfitableProducts(limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/popular/{limit}")
    public ResponseEntity<List<ProductDetailResponse>> getMostPopularProducts(Authentication authentication, @PathVariable int limit) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        List<ProductDetailResponse> products = orderService.getMostPopularProducts(limit);
        return ResponseEntity.ok(products);
    }
}

