package com.example.email.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailDao extends JpaRepository<Email, Long> {

    List<Email> findByState(String state);

    List<Email> findByEmailFrom(String emailFrom);
}
