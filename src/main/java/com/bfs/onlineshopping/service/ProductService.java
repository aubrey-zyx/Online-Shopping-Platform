package com.bfs.onlineshopping.service;

import com.bfs.onlineshopping.dao.ProductDao;
import com.bfs.onlineshopping.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Transactional(readOnly = true)
    public List<Product> getAvailableProducts() {
        return productDao.getAvailableProducts();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productDao.getProductById(id);
    }

    @Transactional
    public boolean updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productDao.getProductById(id);
        if (existingProduct == null) {
            return false;
        }
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setWholesalePrice(updatedProduct.getWholesalePrice());
        existingProduct.setRetailPrice(updatedProduct.getRetailPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        productDao.update(existingProduct);
        return true;
    }

    @Transactional
    public void createProduct(Product newProduct) {
        productDao.add(newProduct);
    }
}

