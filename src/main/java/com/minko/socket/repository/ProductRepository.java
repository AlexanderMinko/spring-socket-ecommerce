package com.minko.socket.repository;

import com.minko.socket.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(Long id, Pageable pageable);

    Page<Product> findByNameContaining(@RequestParam("name") String name, Pageable pageable);

//    Optional<Product> findById(Long id);

    Integer countByCategoryId(Long id);
}
