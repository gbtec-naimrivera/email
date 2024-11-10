package com.example.email.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface EmailCCDao extends JpaRepository<EmailCC, Long> {
}
