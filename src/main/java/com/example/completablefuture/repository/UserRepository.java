package com.example.completablefuture.repository;

import com.example.completablefuture.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
