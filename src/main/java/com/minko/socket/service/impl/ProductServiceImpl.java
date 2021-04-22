package com.minko.socket.service.impl;

import com.minko.socket.entity.Category;
import com.minko.socket.entity.Product;
import com.minko.socket.exception.SocketException;
import com.minko.socket.repository.CategoryRepository;
import com.minko.socket.repository.ProductRepository;
import com.minko.socket.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProducts(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(pageMinus(page), size);
        Page<Product> products = productRepository.findAll(pageRequest);
        log.info("In getProducts - {} products found, page: {}, size: {}",
            products.getTotalElements(), page, size);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        log.info("In getCategories - {} categories found", categories.size());
        return categories;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategoryId(Long id, Integer page,
        Integer size) {
        PageRequest pageRequest = PageRequest.of(pageMinus(page), size);
        Page<Product> products = productRepository
            .findByCategoryId(id, pageRequest);
        log.info(
            "In getProductsByCategoryId - {} products found, by category id: {}, page: {}, size: {} ",
            products.getTotalElements(), id, page, size);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsByNameContaining(String name, Integer page,
        Integer size) {
        PageRequest pageRequest = PageRequest.of(pageMinus(page), size);
        Page<Product> products = productRepository
            .findByNameContaining(name, pageRequest);
        log.info(
            "In getProductsByNameContaining - {} products found, with name containing: {}",
            products.getTotalElements(), name);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
            () -> new SocketException("product not found with id - " + id));
        log.info("In getProductById - product: {} found", product);
        return product;

    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsSortedByPriceAsc(Integer page,
        Integer size) {
        PageRequest pageRequest = PageRequest
            .of(pageMinus(page), size, Sort.by(Sort.Direction.ASC, "price"));
        Page<Product> products = productRepository.findAll(pageRequest);
        log.info(
            "In getProductsSortedByPriceAsc - {} products found, page: {}, size: {}",
            products.getTotalElements(), page, size);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsSortedByPriceDesc(Integer page,
        Integer size) {
        PageRequest pageRequest = PageRequest
            .of(pageMinus(page), size, Sort.by(Sort.Direction.DESC, "price"));
        Page<Product> products = productRepository.findAll(pageRequest);
        log.info(
            "In getProductsSortedByPriceDesc - {} products found, page: {}, size: {}",
            products.getTotalElements(), page, size);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsByNameSorted(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest
            .of(pageMinus(page), size, Sort.by(Sort.Direction.ASC, "name"));
        Page<Product> products = productRepository.findAll(pageRequest);
        log.info(
            "In getProductsByNameSorted - {} products found, page: {}, size: {}",
            products.getTotalElements(), page, size);
        return products;
    }

    //Spring data numbered pages from 0, and this method make page numbering from one
    private Integer pageMinus(Integer page) {
        if (page > 0) {
            return --page;
        } else {
            return 0;
        }
    }
}
