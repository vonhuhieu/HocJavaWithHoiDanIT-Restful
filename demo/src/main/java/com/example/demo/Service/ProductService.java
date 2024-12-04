package com.example.demo.Service;

import com.example.demo.Domain.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    public List<Product> getProducts(){
        return Arrays.asList(
                new Product(1, "Laptop", 100),
                new Product(2, "Smartphone", 80),
                new Product(3, "Tablet", 500)
        );
    }
}
