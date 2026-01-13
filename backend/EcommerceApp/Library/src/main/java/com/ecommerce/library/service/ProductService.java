package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product findById(Long id);
    List<Product> findAll();
    List<Product> getProductsBySubCategoryId(Long id);
    Product save(MultipartFile imageProduct, ProductDto product);
    Product update(MultipartFile imageProduct, ProductDto productDto);

    void enableById(Long id);
    void deleteById(Long id);

    List<ProductDto> products();
    List<ProductDto> allProduct();

    ProductDto getById(Long id);
    List<ProductDto> findByCategoryId(Long id);
    List<ProductDto> searchProducts(String keyword);
    int reduceProductStock(Long id, int qty);

    // for admin
    Page<ProductDto> searchProducts(int pageNo, String keyword);
    Page<ProductDto> getAllProducts(int pageNo);

    // for customer
    Page<ProductDto> getProductsByCategoryPaginated(Long categoryId, int page, int size);
    Page<ProductDto> getProductsBySubCategoryPaginated(Long subCategoryId, int page, int size);

    Page<ProductDto> getAllProductsPaged(int page, int size);
}
