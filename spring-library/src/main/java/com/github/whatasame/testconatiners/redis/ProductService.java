package com.github.whatasame.testconatiners.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(final Product product) {
        return productRepository.save(product);
    }

    public Product getProduct(final String id) {
        return productRepository.findById(id).orElseThrow();
    }
}
