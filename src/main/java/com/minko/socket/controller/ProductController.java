package com.minko.socket.controller;

import com.minko.socket.dto.ReviewRequestDto;
import com.minko.socket.dto.ReviewResponseDto;
import com.minko.socket.entity.Category;
import com.minko.socket.entity.Product;
import com.minko.socket.service.ProductService;
import com.minko.socket.service.ReviewService;
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
    private final ReviewService reviewService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(@PathVariable Long id) {
        return new ResponseEntity<>(reviewService.getAllReviewByProductId(id), HttpStatus.OK);
    }

    @PostMapping("/reviews")
    public ResponseEntity<String> postReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        reviewService.createReview(reviewRequestDto);
        return new ResponseEntity<>("Review successfully created!", HttpStatus.CREATED);
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getReviewByAccountEmail(@RequestParam String email) {
        return new ResponseEntity<>(reviewService.getAllReviewByEmail(email), HttpStatus.OK);
    }

}
