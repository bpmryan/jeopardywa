/*
 * CategoryRepo.java
 * Purpose: Spring Data JPA repository for Category entities. Provides
 * methods to find categories by gameId and perform CRUD operations.
 */
package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Category;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, String> {

    // This is so that the java functions can interact with the db
    List<Category> findByGameId(String gameId);
}
