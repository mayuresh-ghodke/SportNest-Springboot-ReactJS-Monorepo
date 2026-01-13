package com.ecommerce.customer.controller;

import com.ecommerce.customer.exception.ResourceNotFoundException;
import com.ecommerce.library.collectionMapper.CollectionMapper;
import com.ecommerce.library.customerDto.ProductResponseDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.dto.ReviewDto;
import com.ecommerce.library.mapper.Mapper;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Review;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product") // prefix for API endpoints
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // React app
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping(value = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
        List<ProductDto> productDtoList = productService.allProduct();
        List<ProductResponseDto> productResponseDtoList = CollectionMapper.mapProductDtoListToProductResponseDtoList(productDtoList);
        return new ResponseEntity<>(productResponseDtoList, HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDto> getProductByProductId(@PathVariable("id") Long productId){
        ProductDto productDto = productService.getById(productId);
        ProductResponseDto productResponseDto = ResponseDtoMapper.mapProductDtoToProductResponseDto(productDto);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/get/by-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategoryId(@RequestParam Long categoryId){
        List<ProductDto> productDtoList = productService.findByCategoryId(categoryId);
        List<ProductResponseDto> productResponseDtoList = CollectionMapper.mapProductDtoListToProductResponseDtoList(productDtoList);
        return new ResponseEntity<>(productResponseDtoList, HttpStatus.OK);
    }

    // PRODUCTS BY SUBCATEGORY
    @GetMapping(value = "/get/by-subcategory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductsBySubCategory(@RequestParam("id") Long subCategoryId) {
        List<Product> products = productService.getProductsBySubCategoryId(subCategoryId);
        List<ProductDto> productsDtoList = products.stream().map(Mapper::productToProductDto).toList();
        List<ProductResponseDto> productResponseDtos = CollectionMapper.mapProductDtoListToProductResponseDtoList(productsDtoList);
        return ResponseEntity.ok(Map.of("products", productResponseDtos));
    }

    // SEARCH PRODUCTS
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchProducts(@RequestParam String keyword) {
        List<ProductDto> productsDtoList = productService.searchProducts(keyword);
        List<ProductResponseDto> productResponseDtos = CollectionMapper.mapProductDtoListToProductResponseDtoList(productsDtoList);
        return ResponseEntity.ok(Map.of("products", productResponseDtos));
    }

    // PRODUCT DETAIL + REVIEWS + RATING STATS
    @GetMapping(value = "/detail/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDto> getProductDetail(
            @PathVariable("id") Long productId) {
        ProductDto productDto = productService.getById(productId);
        if(productDto == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        ProductResponseDto productResponseDto = ResponseDtoMapper.mapProductDtoToProductResponseDto(productDto);
        return ResponseEntity.ok(productResponseDto);
    }

    // pagination results
    @GetMapping(value = "/get/by-category/paged", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductsByCategoryPaged(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size){

        Page<ProductDto> productPage = productService.getProductsByCategoryPaginated(categoryId, page, size);
        List<ProductResponseDto> productResponseDtos =
                CollectionMapper.mapProductDtoListToProductResponseDtoList(productPage.getContent());

        return ResponseEntity.ok(Map.of(
                "products", productResponseDtos,
                "currentPage", productPage.getNumber(),
                "totalPages", productPage.getTotalPages(),
                "totalElements", productPage.getTotalElements()
        ));
    }

    @GetMapping(value = "/get/by-subcategory/paged", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductsBySubCategoryPaged(
            @RequestParam Long subCategoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ){
        Page<ProductDto> productPage =
                productService.getProductsBySubCategoryPaginated(subCategoryId, page, size);

        List<ProductResponseDto> products =
                CollectionMapper.mapProductDtoListToProductResponseDtoList(
                        productPage.getContent()
                );

        return ResponseEntity.ok(Map.of(
                "products", products,
                "currentPage", productPage.getNumber(),
                "totalPages", productPage.getTotalPages(),
                "totalElements", productPage.getTotalElements()
        ));
    }

    @GetMapping(value = "/get/all/paged", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size){

        Page<ProductDto> productPage = productService.getAllProductsPaged(page, size);
        List<ProductResponseDto> productResponseDtos =
                CollectionMapper.mapProductDtoListToProductResponseDtoList(productPage.getContent());

        return ResponseEntity.ok(Map.of(
                "products", productResponseDtos,
                "currentPage", productPage.getNumber(),
                "totalPages", productPage.getTotalPages(),
                "totalElements", productPage.getTotalElements()
        ));
    }
}
