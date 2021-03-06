package com.minko.socket.service;

import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;
import com.minko.socket.entity.Category;
import com.minko.socket.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Page<Product> getProducts(Integer page, Integer size);

    List<Category> getCategories();

    Page<Product> getProductsByCategoryId(Long id, Integer page, Integer size);

    Page<Product> getProductsByNameContaining(String name, Integer page, Integer size);

    Product getProductById(Long id);

    Page<Product> getProductsSortedByPriceAsc(Integer page, Integer size);

    Page<Product> getProductsSortedByPriceDesc(Integer page, Integer size);

    Page<Product> getProductsByNameSorted(Integer page, Integer size);

}
