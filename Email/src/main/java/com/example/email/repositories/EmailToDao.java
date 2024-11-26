package com.example.email.repositories;

import com.example.email.entity.EmailTo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailToDao extends JpaRepository<EmailTo, Long> {
}
