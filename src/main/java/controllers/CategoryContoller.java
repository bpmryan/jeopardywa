package controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import model.Category;
import repo.CategoryRepo;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryContoller {
    
    @Autowired
    private CategoryRepo categoryRepo;

    // Java function to create CategoryId and save to db
    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        category.setCategoryId("C" + String.format("%05d", (int)(Math.random() *100000)));
        return categoryRepo.save(category);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
}
