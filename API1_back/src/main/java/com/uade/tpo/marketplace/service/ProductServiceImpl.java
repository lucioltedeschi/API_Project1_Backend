
package com.uade.tpo.marketplace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.controllers.products.ProductUpdateRequest;
import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.exceptions.Category.CategoryNotFoundException;
import com.uade.tpo.marketplace.exceptions.Product.ProductDuplicateException;
import com.uade.tpo.marketplace.exceptions.Product.ProductNotFoundException;
import com.uade.tpo.marketplace.repository.CategoryRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Product> getAllProducts(PageRequest pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Product createProduct(String name, String description, Float price, Integer stock, Category category) throws ProductDuplicateException {
        List<Product> products = productRepository.findByProductByName(name);
        if (products.isEmpty())
            return productRepository.save(new Product(name, description, price, stock, category));
        throw new ProductDuplicateException();
    }

    @Override
    public void deleteProduct(Long productId) throws ProductNotFoundException{
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent())
            productRepository.deleteById(productId);
        else
            throw new ProductNotFoundException();
    }

    @Override
    public Product updateProduct(Long productId, ProductUpdateRequest updateRequest)
            throws ProductNotFoundException, ProductDuplicateException, CategoryNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        Product p;
        if (product.isPresent()) {
            p = product.get();
            if (updateRequest.getName() != null) {
                List<Product> products = productRepository.findByProductByName(updateRequest.getName());
                if (products.isEmpty())
                    p.setName(updateRequest.getName());
                else
                    throw new ProductDuplicateException();
            }
            if (updateRequest.getDescription() != null) {
                p.setDescription(updateRequest.getDescription());
            }
            if (updateRequest.getPrice() != null) {
                p.setPrice(updateRequest.getPrice());
            }
            if (updateRequest.getStock() != null) {
                if (updateRequest.getStock() == 0) {
                    productRepository.deleteById(productId);
                    return p;
                }
                p.setStock(updateRequest.getStock());
            }
            if (updateRequest.getCategoryId() != null) {
                Optional<Category> category = categoryRepository.findById(updateRequest.getCategoryId());
                Category c;
                if (category.isPresent()) {
                    c = category.get();
                    p.setCategory(c);
                }
                throw new CategoryNotFoundException();
            }
            return productRepository.save(p);
        }
        throw new ProductNotFoundException();

    }

}
