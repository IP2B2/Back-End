package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.dao.AuditLog;
import com.UAIC.ISMA.dao.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

