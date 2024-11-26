package com.example.email.repositories;

import com.example.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailDao extends JpaRepository<Email, Long> {

    List<Email> findByState(int state);

    List<Email> findByEmailFrom(String emailFrom);

}
