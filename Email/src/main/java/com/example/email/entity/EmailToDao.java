package com.example.email.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EmailToDao extends JpaRepository<EmailTo, Long> {
}
