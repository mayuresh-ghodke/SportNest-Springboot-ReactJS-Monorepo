package com.ecommerce.customer.controller;

import com.ecommerce.library.customerDto.ProductResponseDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.mapper.Mapper;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Wishlist;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // React app
public class WishlistController {

    private final ProductService productService;
    private final WishlistService wishlistService;
    private final CustomerService customerService;

    // GET ALL WISHLIST PRODUCTS
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponseDto>> getWishlist(Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long customerId = customerService.getCustomerId(principal.getName());
        List<Product> products = wishlistService.getWishlistByCustomerId(customerId)
                .stream()
                .map(Wishlist::getProduct).toList();
        List<ProductDto> productDtoList = products.stream().map(Mapper::productToProductDto).toList();
        List<ProductResponseDto> productResponseDtos = productDtoList.stream().map(ResponseDtoMapper::mapProductDtoToProductResponseDto).toList();
        return ResponseEntity.ok(productResponseDtos);
    }

    // ADD TO WISHLIST
    @PostMapping(value = "/add/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addToWishlist(@PathVariable Long productId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not logged in"));
        }
        Product product = productService.findById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Product not found"));
        }
        Long customerId = customerService.getCustomerId(principal.getName());
        Customer customer = customerService.getCustomerById(customerId);
        Wishlist existing = wishlistService.getByCustomerIdAndProductId(customerId, productId);
        if (existing != null) {
            return ResponseEntity.ok(Map.of("message", product.getName() + " already in wishlist"));
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);
        wishlist.setProduct(product);
        wishlistService.save(wishlist);
        return ResponseEntity.ok(Map.of("message", product.getName() + " added to wishlist"));
    }

    // DELETE FROM WISHLIST
    @DeleteMapping(value = "/delete/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long productId, Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long customerId = customerService.getCustomerId(principal.getName());
        Wishlist wishlist = wishlistService.getByCustomerIdAndProductId(customerId, productId);
        if (wishlist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Item not found"));
        }
        wishlistService.deleteById(wishlist.getId());
        return ResponseEntity.ok(Map.of("message", "Removed from wishlist"));
    }
}
