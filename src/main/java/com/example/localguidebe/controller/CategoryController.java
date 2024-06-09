package com.example.localguidebe.controller;

import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.service.CategoryService;
import com.example.localguidebe.system.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoryService categoryService;
    @Autowired
    public void setCategoryService(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    @GetMapping("")
    public ResponseEntity<Result> getAllCategory(){
        try {
            return new ResponseEntity<>(new Result(true, HttpStatus.OK.value(), "get the list successfully",  categoryService.getCategories()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new Result(false, HttpStatus.CONFLICT.value(), "get the failure list", null), HttpStatus.CONFLICT);
        }
    }
}
