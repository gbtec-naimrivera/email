package com.example.email.repositories;

import com.example.email.entity.EmailTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>Repository interface for managing {@link EmailTo} entities.</p>
 * <p>This interface provides CRUD operations for the {@link EmailTo} entity, using {@link JpaRepository}.</p>
 */
@Repository
public interface EmailToDao extends JpaRepository<EmailTo, Long> {
}
