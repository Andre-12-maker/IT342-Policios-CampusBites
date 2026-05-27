package com.campusbites.product.service;

import com.campusbites.common.exception.AppException;
import com.campusbites.product.dto.ProductDtos;
import com.campusbites.product.model.Product;
import com.campusbites.product.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) { this.productRepository = productRepository; }

    public Page<ProductDtos.ProductResponse> getProducts(String category, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        boolean hasCategory = StringUtils.hasText(category) && !category.equalsIgnoreCase("All");
        boolean hasSearch   = StringUtils.hasText(search);
        Page<Product> results;
        if (hasCategory && hasSearch)  results = productRepository.searchByCategoryAndName(category, search, pageable);
        else if (hasCategory)          results = productRepository.findByCategoryIgnoreCaseAndAvailableTrue(category, pageable);
        else if (hasSearch)            results = productRepository.searchByName(search, pageable);
        else                           results = productRepository.findByAvailableTrue(pageable);
        return results.map(this::toResponse);
    }

    public ProductDtos.ProductResponse getProduct(String id) { return toResponse(findById(id)); }

    public ProductDtos.ProductResponse createProduct(ProductDtos.CreateProductRequest req) {
        Product p = new Product();
        p.setName(req.name()); p.setDescription(req.description()); p.setCategory(req.category());
        p.setPrice(req.price()); p.setImageUrl(req.imageUrl()); p.setStock(req.stock());
        p.setAvailable(req.stock() > 0);
        return toResponse(productRepository.save(p));
    }

    public ProductDtos.ProductResponse updateProduct(String id, ProductDtos.UpdateProductRequest req) {
        Product p = findById(id);
        if (StringUtils.hasText(req.name()))        p.setName(req.name());
        if (StringUtils.hasText(req.description())) p.setDescription(req.description());
        if (StringUtils.hasText(req.category()))    p.setCategory(req.category());
        if (req.price()     != null)                p.setPrice(req.price());
        if (StringUtils.hasText(req.imageUrl()))    p.setImageUrl(req.imageUrl());
        if (req.stock()     != null)                p.setStock(req.stock());
        if (req.available() != null)                p.setAvailable(req.available());
        return toResponse(productRepository.save(p));
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) throw AppException.notFound("Product not found");
        productRepository.deleteById(id);
    }

    public Product findById(String id) {
        return productRepository.findById(id).orElseThrow(() -> AppException.notFound("Product not found"));
    }

    public ProductDtos.ProductResponse toResponse(Product p) {
        return new ProductDtos.ProductResponse(p.getId(), p.getName(), p.getDescription(),
                p.getCategory(), p.getPrice(), p.getImageUrl(), p.getRating(),
                p.getRatingCount(), p.isAvailable(), p.getStock());
    }
}