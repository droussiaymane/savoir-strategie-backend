package com.savoirstrategie.app.repository;

import com.savoirstrategie.app.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
}
