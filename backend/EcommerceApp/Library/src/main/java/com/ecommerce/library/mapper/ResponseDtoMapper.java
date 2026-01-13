package com.ecommerce.library.mapper;

import com.ecommerce.library.customerDto.*;
import com.ecommerce.library.dto.*;
import com.ecommerce.library.model.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ResponseDtoMapper {

    private ResponseDtoMapper(){}

    // map Customer to CustomerResponseDto
    public static CustomerResponseDto mapCustomerDtoToCustomerResponseDto(CustomerDto customerDto){
        return new CustomerResponseDto(
                customerDto.getFirstName(),
                customerDto.getLastName(),
                customerDto.getUsername(),
                customerDto.getPhoneNumber()
        );
    }

    // map Address to AddressResponseDto
    public static AddressResponseDto mapAddressToAddressResponseDto(Address address){
        return new AddressResponseDto(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getPincode(),
                address.getState(),
                address.getCountry(),
                address.getCustomer().getFirstName(),
                address.getCustomer().getLastName()
        );
    }

    // map CategoryDto to CategoryResponseDto
    public static CategoryResponseDto mapCategoryDtoToCategoryResponseDto(CategoryDto categoryDto){
        return new CategoryResponseDto(
                categoryDto.getId(),
                categoryDto.getName()
        );
    }

    // map SubCategoryDto to SubCategoryResponseDto
    public static SubCategoryResponseDto mapSubCategoryDtoToSubCategoryResponseDto(SubCategoryDto subCategoryDto){
        return new SubCategoryResponseDto(
                subCategoryDto.getId(),
                subCategoryDto.getName()
        );
    }

    // map ProductDto to ProductResponseDto
    public static ProductResponseDto mapProductDtoToProductResponseDto(ProductDto productDto){
        return new ProductResponseDto(
                productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getCurrentQuantity(),
                productDto.getCostPrice(),
                productDto.getImage(),
                productDto.getCategory().getId(),
                productDto.getCategory().getName(),
                productDto.getSubCategory().getId(),
                productDto.getSubCategory().getName(),
                productDto.isActivated(),
                productDto.isDeleted()
        );
    }

    public static OrderDetailResponseDto mapOrderDetailDtoToOrderDetailResponeDto(OrderDetailDto orderDetailDto){
        return new OrderDetailResponseDto(
                orderDetailDto.getProductId(),
                orderDetailDto.getProductName(),
                orderDetailDto.getProductImage(),
                orderDetailDto.getPrice(),
                orderDetailDto.getQuantity(),
                orderDetailDto.getSubTotal()
        );
    }


    // map OrderDto to OrderResponseDto with payment data null
    public static OrderResponseDto mapOrderDtoToOrderResponseDto(OrderDto orderDto) {

        OrderResponseDto response = new OrderResponseDto();

        response.setOrderId(orderDto.getOrderId());
        response.setOrderDate(orderDto.getOrderDate());
        response.setDeliveryDate(orderDto.getDeliveryDate());
        response.setOrderStatus(orderDto.getOrderStatus());
        response.setAccepted(orderDto.isAssigned());
        response.setDelivered(orderDto.isDelivered());
        response.setTotalPrice(orderDto.getTotalPrice());
        response.setQuantity(orderDto.getTotalQuantity());

        // Order items
        List<OrderDetailResponseDto> items =
                orderDto.getItems()
                        .stream()
                        .map(ResponseDtoMapper::mapOrderDetailDtoToOrderDetailResponeDto)
                        .toList();

        response.setItems(items);

        // Delivery person
        if (orderDto.getDeliveryPerson() != null) {
            response.setDeliveryPersonName(
                    orderDto.getDeliveryPerson().getFirstName() + " " +
                            orderDto.getDeliveryPerson().getLastName()
            );
        }

        return response;
    }

    // map OrderDto to OrderResponseDto with payment data.
    public static OrderResponseDto mapOrderDtoToOrderResponseDto
    (OrderDto orderDto, PaymentOrderResponseDto paymentOrderResponseDto) {

        OrderResponseDto response = new OrderResponseDto();

        response.setOrderId(orderDto.getOrderId());
        response.setOrderDate(orderDto.getOrderDate());
        response.setDeliveryDate(orderDto.getDeliveryDate());
        response.setOrderStatus(orderDto.getOrderStatus());
        response.setAccepted(orderDto.isAssigned());
        response.setDelivered(orderDto.isDelivered());
        response.setTotalPrice(orderDto.getTotalPrice());
        response.setQuantity(orderDto.getTotalQuantity());

        // Order items
        List<OrderDetailResponseDto> items =
                orderDto.getItems()
                        .stream()
                        .map(ResponseDtoMapper::mapOrderDetailDtoToOrderDetailResponeDto)
                        .toList();

        response.setItems(items);

        // Delivery person
        if (orderDto.getDeliveryPerson() != null) {
            response.setDeliveryPersonName(
                    orderDto.getDeliveryPerson().getFirstName() + " " +
                            orderDto.getDeliveryPerson().getLastName()
            );
        }

        // Payment info (latest payment)
        if (paymentOrderResponseDto != null) {
            response.setPaymentId(paymentOrderResponseDto.getPaymentId());
            response.setPaymentMethod("RAZORPAY");
            response.setPaymentStatus(paymentOrderResponseDto.getStatus());
        }
        return response;
    }

    // map CartItemDto to CartItemResponseDto
    public static CartItemResponseDto mapCartItemDtoToCartItemResponseDto(CartItemDto cartItemDto){
        return new CartItemResponseDto(
                cartItemDto.getId(),
                cartItemDto.getProduct().getId(),
                cartItemDto.getProduct().getName(),
                cartItemDto.getProduct().getImage(),
                cartItemDto.getUnitPrice(),
                cartItemDto.getQuantity(),
                cartItemDto.getUnitPrice() * cartItemDto.getQuantity()
        );
    }

    // map ShoppingCartDto to ShoppingCartResponseDto
    public static ShoppingCartResponseDto mapShoppingCartDtoToShoppingCartResponseDto(ShoppingCartDto shoppingCartDto){

        Set<CartItemResponseDto> cartItems = shoppingCartDto.getCartItems()
                .stream()
                .map(ResponseDtoMapper::mapCartItemDtoToCartItemResponseDto)
                .collect(Collectors.toSet());

        return new ShoppingCartResponseDto(
                shoppingCartDto.getId(),
                shoppingCartDto.getTotalPrice(),
                shoppingCartDto.getTotalItems(),
                cartItems
        );
    }

    // map PaymentOrder to PaymentOrderResponseDto
    public static PaymentOrderResponseDto mapPaymentOrderToPaymentOrderResponseDto(PaymentOrder paymentOrder){
        return new PaymentOrderResponseDto(
                paymentOrder.getPaymentOrderId(),
                paymentOrder.getAmount(),
                paymentOrder.getReceipt(),
                paymentOrder.getPaymentStatus(),
                paymentOrder.getCustomer().getFirstName(),
                paymentOrder.getCustomer().getLastName(),
                paymentOrder.getPaymentId(),
                paymentOrder.getPaymentCreatedAt(),
                paymentOrder.getPaymentCompletedAt()
        );
    }

    // map ReviewDto to ReviewResponseDto
    // Convert entity to DTO
    public static ReviewResponseDto mapReviewToReviewResponseDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setReviewId(review.getReviewId());
        dto.setRatingNumber(review.getRatingNumber());
        dto.setFeedback(review.getFeedback());
        dto.setProductId(review.getProduct().getId());
        dto.setProductName(review.getProduct().getName());
        dto.setCustomerId(review.getCustomer().getId());
        dto.setCustomerName(review.getCustomer().getFirstName() + " " + review.getCustomer().getLastName());
        return dto;
    }


    // map Wishlist to WishlistResponseDto
    public static WishlistResponseDto mapWishlistToWishlistResponseDto(Wishlist wishlist){
        return new WishlistResponseDto(
                wishlist.getId(),
                wishlist.getProduct().getId(),
                wishlist.getProduct().getName(),
                wishlist.getProduct().getCostPrice(),
                wishlist.getProduct().getCurrentQuantity(),
                wishlist.getProduct().getImage(),
                wishlist.getProduct().getCurrentQuantity() > 0
        );
    }
}
