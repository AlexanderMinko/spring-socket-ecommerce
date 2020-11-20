package com.minko.socket.controller;

import com.minko.socket.entity.Category;
import com.minko.socket.entity.Product;
import com.minko.socket.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(@RequestParam Integer page, @RequestParam Integer size) {
        return new ResponseEntity<>(productService.getProducts(page, size), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        return new ResponseEntity<>(productService.getCategories(), HttpStatus.OK);
    }

    @GetMapping("/by-category")
    public ResponseEntity<Page<Product>> getProductsByCategoryId(
            @RequestParam Long id, @RequestParam Integer page, @RequestParam Integer size) {
        return new ResponseEntity<>(productService.getProductsByCategoryId(id, page, size), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> getProductsByNameContaining(
            @RequestParam String name, @RequestParam Integer page, @RequestParam Integer size) {
        return new ResponseEntity<>(productService.getProductsByNameContaining(name, page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/by-price-asc")
    public ResponseEntity<Page<Product>> getProductsSorterByPriceAsc(
            @RequestParam Integer page, @RequestParam Integer size) {
        return new ResponseEntity<>(productService.getProductsSortedByPriceAsc(page, size), HttpStatus.OK);
    }

    @GetMapping("/by-price-desc")
    public ResponseEntity<Page<Product>> getProductsSorterByPriceDesc(
            @RequestParam Integer page, @RequestParam Integer size) {
        return new ResponseEntity<>(productService.getProductsSortedByPriceDesc(page, size), HttpStatus.OK);
    }

    @GetMapping("/by-name")
    public ResponseEntity<Page<Product>> getProductsByNameSorted(
            @RequestParam Integer page, @RequestParam Integer size) {
        return new ResponseEntity<>(productService.getProductsByNameSorted(page, size), HttpStatus.OK);
    }

}
