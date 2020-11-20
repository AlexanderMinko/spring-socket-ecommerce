package com.minko.socket.repository;

import com.minko.socket.entity.Review;
import com.minko.socket.entity.SubReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SubReviewRepositoryTest {

    @Autowired
    private SubReviewRepository subReviewRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private SubReview savedSubReview;
    private Review savedReview;

    @BeforeEach
    void setUp() {
        Review review = new Review(null, "name", Instant.now(), null, null);
        SubReview subReview = new SubReview(null, "text", Instant.now(), null, review);
        savedReview = reviewRepository.save(review);
        savedSubReview = subReviewRepository.save(subReview);
    }

    @Test
    void findByReviewId() {
        List<SubReview> subReviewsAct = subReviewRepository.findByReviewId(savedReview.getId());
        assertThat(subReviewsAct).isEqualTo(Collections.singletonList(savedSubReview));
    }

    @Test
    void countByReviewId() {
        Integer count = subReviewRepository.countByReviewId(savedReview.getId());
        assertThat(count).isEqualTo(1);
    }
}