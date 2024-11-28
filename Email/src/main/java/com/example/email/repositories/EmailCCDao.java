package com.example.email.repositories;

import com.example.email.entity.EmailCC;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>Repository interface for managing {@link EmailCC} entities.</p>
 * <p>This interface provides CRUD operations for the {@link EmailCC} entity, using {@link JpaRepository}.</p>
 */
public interface EmailCCDao extends JpaRepository<EmailCC, Long> {
}
