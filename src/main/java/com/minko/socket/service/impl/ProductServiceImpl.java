package com.minko.socket.service.impl;

import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Category;
import com.minko.socket.entity.Product;
import com.minko.socket.entity.Review;
import com.minko.socket.exception.SocketException;
import com.minko.socket.mapper.ReviewMapper;
import com.minko.socket.repository.CategoryRepository;
import com.minko.socket.repository.ProductRepository;
import com.minko.socket.repository.ReviewRepository;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<Product> getProducts(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(pageMinus(page), size);
        return productRepository.findAll(pageRequest);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Page<Product> getProductsByCategoryId(Long id, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(pageMinus(page), size);
        return productRepository.findByCategoryId(id, pageRequest);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new SocketException("product not found with id - " + id));
    }

    //Spring data numbered pages from 0, and this method make page numbering from one
    private Integer pageMinus(Integer page) {
        if(page > 0) {
            return --page;
        } else {
            return 0;
        }
    }
}
