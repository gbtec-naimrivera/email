package com.example.email.repositories;

import com.example.email.entity.EmailCC;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCCDao extends JpaRepository<EmailCC, Long> {
}
