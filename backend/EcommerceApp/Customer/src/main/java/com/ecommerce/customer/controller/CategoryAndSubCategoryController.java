package com.ecommerce.customer.controller;

import com.ecommerce.library.collectionMapper.CollectionMapper;
import com.ecommerce.library.customerDto.CategoryResponseDto;
import com.ecommerce.library.customerDto.SubCategoryResponseDto;
import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.mapper.Mapper;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.dto.SubCategoryDto;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.SubCategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api") // prefix for API endpoints
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // React app
public class CategoryAndSubCategoryController {

    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    // GET ALL CATEGORIES
    @GetMapping(value = "/category/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        List<CategoryDto> categoryDtoList = categoryService.getCategoriesAndSize();
        List<CategoryResponseDto> categoryResponseDtos = CollectionMapper.mapCategoryDtoListToCategoryResponseDtoList(categoryDtoList);
        return ResponseEntity.ok(categoryResponseDtos);
    }

    // GET ALL SUBCATEGORIES
    @GetMapping(value = "/subcategory/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SubCategoryResponseDto>> getAllSubCategories() throws Exception {
        List<SubCategoryDto> subCategoryDtoList = subCategoryService.findAllSubCategories().stream()
                .map(Mapper::mapSubCategoryToSubCategoryDto).toList();
        List<SubCategoryResponseDto> subCategoryResponseDtos =  subCategoryDtoList.stream().map(ResponseDtoMapper::mapSubCategoryDtoToSubCategoryResponseDto).toList();
        return ResponseEntity.ok(subCategoryResponseDtos);
    }

    // GET SUBCATEGORIES BY CATEGORY ID
    @GetMapping(value = "/subcategory/get/all/by-category/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SubCategoryResponseDto>> getSubcategoriesByCategoryId(@PathVariable("id") Long categoryId) {
        List<SubCategoryDto> dtoList = subCategoryService.getAllSubCategoriesByCategoryId(categoryId)
                .stream()
                .map(Mapper::mapSubCategoryToSubCategoryDto).toList();
        List<SubCategoryResponseDto> subCategoryResponseDtos = dtoList.stream().map(ResponseDtoMapper::mapSubCategoryDtoToSubCategoryResponseDto).toList();
        return ResponseEntity.ok(subCategoryResponseDtos);
    }
}