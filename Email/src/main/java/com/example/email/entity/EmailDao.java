package com.example.email.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EmailDao extends JpaRepository<Email, Long> {

    List<Email> findByState(int state);

    List<Email> findByEmailFrom(String emailFrom);
}
