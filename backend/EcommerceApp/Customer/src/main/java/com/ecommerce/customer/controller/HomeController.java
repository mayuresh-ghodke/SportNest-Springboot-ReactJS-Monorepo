package com.ecommerce.customer.controller;

import com.ecommerce.customer.exception.InvalidPhoneNumberException;
import com.ecommerce.customer.exception.ResourceNotFoundException;
import com.ecommerce.library.customerDto.*;
import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.mapper.Mapper;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api") // prefix for API endpoints
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // React app
public class HomeController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final SubCategoryService subCategoryService;
    private final OrderService orderService;

    @GetMapping(value = "/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CatalogResponseDto> getCatalogOrShoppingPage() {

        // Fetch categories
        List<CategoryDto> categoryDtoList = categoryService.getCategoriesAndSize();

        // Fetch products
        List<ProductDto> productDtoList = productService.allProduct();
        List<ProductResponseDto> productResponseDtoList = productDtoList.stream()
                .map(ResponseDtoMapper::mapProductDtoToProductResponseDto).toList();

        // Map categories and subcategories
        List<CategoryWithSubCategoryResponseDto> categoryWithSubCategoryResponseDtoList = categoryDtoList.stream()
                .map(category -> {
                    List<SubCategoryResponseDto> subCategories = subCategoryService
                            .getAllSubCategoriesByCategoryId(category.getId())
                            .stream()
                            .map(sub -> new SubCategoryResponseDto(sub.getId(), sub.getName()))
                            .toList();
                    return new CategoryWithSubCategoryResponseDto(
                            category.getId(),
                            category.getName(),
                            subCategories
                    );
                })
                .toList();

        // Build final CatalogResponseDto
        CatalogResponseDto catalogResponseDto = new CatalogResponseDto();
        catalogResponseDto.setCategoryWithSubCategoryResponseDtoList(categoryWithSubCategoryResponseDtoList);
        catalogResponseDto.setProductResponseDtoList(productResponseDtoList);

        // Return response
        return ResponseEntity.ok(catalogResponseDto);
    }
}
