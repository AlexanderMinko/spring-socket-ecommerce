package com.minko.socket.repository;

import com.minko.socket.entity.SubReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubReviewRepository extends JpaRepository<SubReview, Long> {

    List<SubReview> findByReviewId(Long id);

    Integer countByReviewId(Long id);
}
