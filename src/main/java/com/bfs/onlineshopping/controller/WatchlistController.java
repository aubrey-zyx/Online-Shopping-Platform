package com.bfs.onlineshopping.controller;

import com.bfs.onlineshopping.domain.response.WatchlistResponse;
import com.bfs.onlineshopping.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<String> addProductToWatchlist(Authentication authentication, @PathVariable Long productId) {
        String username = authentication.getName();
        boolean added = watchlistService.addProductToWatchlist(username, productId);
        if (!added) {
            return ResponseEntity.badRequest().body("Product is already in the watchlist.");
        }
        return ResponseEntity.ok("Product added to watchlist.");
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> removeProductFromWatchlist(Authentication authentication, @PathVariable Long productId) {
        String username = authentication.getName();
        boolean removed = watchlistService.removeProductFromWatchlist(username, productId);
        if (!removed) {
            return ResponseEntity.badRequest().body("Product is not in the watchlist.");
        }
        return ResponseEntity.ok("Product removed from watchlist.");
    }

    @GetMapping("/products/all")
    public ResponseEntity<List<WatchlistResponse>> getWatchlistProducts(Authentication authentication) {
        String username = authentication.getName();
        List<WatchlistResponse> products = watchlistService.getWatchlistProducts(username);
        return ResponseEntity.ok(products);
    }
}
