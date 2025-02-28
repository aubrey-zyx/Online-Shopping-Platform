package com.bfs.onlineshopping.service;

import com.bfs.onlineshopping.dao.ProductDao;
import com.bfs.onlineshopping.dao.UserDao;
import com.bfs.onlineshopping.dao.WatchlistDao;
import com.bfs.onlineshopping.domain.Product;
import com.bfs.onlineshopping.domain.User;
import com.bfs.onlineshopping.domain.Watchlist;
import com.bfs.onlineshopping.domain.response.WatchlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchlistService {

    private final WatchlistDao watchlistDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public WatchlistService(WatchlistDao watchlistDao, UserDao userDao, ProductDao productDao) {
        this.watchlistDao = watchlistDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @Transactional
    public boolean addProductToWatchlist(String username, Long productId) {
        User user = userDao.loadUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productDao.getProductById(productId);
        if (product == null || watchlistDao.existsByUserAndProduct(user, product)) {
            return false;
        }
        watchlistDao.add(new Watchlist(user, product));
        return true;
    }

    @Transactional
    public boolean removeProductFromWatchlist(String username, Long productId) {
        User user = userDao.loadUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productDao.getProductById(productId);
        if (product == null || !watchlistDao.existsByUserAndProduct(user, product)) {
            return false;
        }
        watchlistDao.removeByUserAndProduct(user, product);
        return true;
    }

    public List<WatchlistResponse> getWatchlistProducts(String username) {
        User user = userDao.loadUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return watchlistDao.findByUser(user).stream()
                .filter(watchlist -> watchlist.getProduct().getQuantity() > 0)
                .map(watchlist -> new WatchlistResponse(watchlist.getProduct().getProductId(), watchlist.getProduct().getName(), watchlist.getProduct().getRetailPrice()))
                .collect(Collectors.toList());
    }
}
