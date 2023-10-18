package com.savoirstrategie.app.repository;

import com.savoirstrategie.app.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
