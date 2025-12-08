/*
 * QnARepo.java
 * Purpose: Spring Data JPA repository for QnA entities. Provides basic CRUD
 * operations via JpaRepository and convenience methods used by the service:
 *  - findByCategoryId(String): load all QnA rows for a category
 *  - deleteByCategoryId(String): delete all QnA rows for a category
 */
package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.QnA;

@Repository
public interface QnARepo extends JpaRepository<QnA, String> {

    // Load all QnA associated with that categoryId

    List<QnA> findByCategoryId(String categoryId);

    void deleteByCategoryId(String categoryId);

}
