package com.ecommerce.customer.controller;

import com.ecommerce.library.customerDto.CartItemResponseDto;
import com.ecommerce.library.customerDto.ShoppingCartResponseDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ShoppingCartController {

    private final ShoppingCartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;

    private Map<String, Object> response(boolean success, String message, Object data) {
        return Map.of(
                "success", success,
                "message", message,
                "data", data
        );
    }

    private ShoppingCartResponseDto buildCartDto(ShoppingCart cart) {

        ShoppingCartResponseDto dto = new ShoppingCartResponseDto();
        dto.setCartId(cart.getId());
        dto.setTotalItems(cart.getTotalItems());
        dto.setTotalPrice(cart.getTotalPrice());

        Set<CartItemResponseDto> items = new HashSet<>();

        for (CartItem item : cart.getCartItems()) {
            CartItemResponseDto itemDto = new CartItemResponseDto();
            itemDto.setId(item.getId());
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setProductImage(item.getProduct().getImage());
            itemDto.setUnitPrice(item.getProduct().getCostPrice());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setTotalPrice(item.getUnitPrice() * item.getQuantity());
            items.add(itemDto);
        }

        dto.setItems(items);
        return dto;
    }

    @GetMapping
    public ResponseEntity<?> getShoppingCart(Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(response(false, "User not logged in", null));
        }

        Customer customer = customerService.findByUsername(principal.getName());
        ShoppingCart cart = customer.getCart();

        if (cart == null || cart.getCartItems().isEmpty()) {
            return ResponseEntity.ok(
                    response(true, "Cart is empty", null)
            );
        }

        return ResponseEntity.ok(
                response(true, "Cart fetched successfully", buildCartDto(cart))
        );
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(response(false, "User not logged in", null));
        }

        ProductDto productDto = productService.getById(productId);
        if (productDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(response(false, "Product not found", null));
        }

        ShoppingCart cart =
                cartService.addItemToCart(productDto, quantity, principal.getName());

        return ResponseEntity.ok(
                response(true, "Item added to cart", buildCartDto(cart))
        );
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(response(false, "User not logged in", null));
        }

        ProductDto productDto = productService.getById(productId);
        ShoppingCart cart =
                cartService.updateCart(productDto, quantity, principal.getName());

        return ResponseEntity.ok(
                response(true, "Cart updated", buildCartDto(cart))
        );
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeItemFromCart(
            @PathVariable Long productId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(response(false, "User not logged in", null));
        }

        ProductDto productDto = productService.getById(productId);
        ShoppingCart cart =
                cartService.removeItemFromCart(productDto, principal.getName());

        return ResponseEntity.ok(
                response(true, "Item removed from cart", buildCartDto(cart))
        );
    }
}
