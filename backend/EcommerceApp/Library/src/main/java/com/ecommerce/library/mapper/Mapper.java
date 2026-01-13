package com.ecommerce.library.mapper;

import com.ecommerce.library.customerDto.CustomerResponseDto;
import com.ecommerce.library.dto.*;
import com.ecommerce.library.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    private Mapper(){}

    public static ProductDto productToProductDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCurrentQuantity(),
                product.getCostPrice(),
                product.getSalePrice(),
                product.getImage(),
                product.getCategory(),
                product.is_activated(),
                product.is_deleted(),
                "",
                product.getSubCategory()
        );
    }

    public static Product productDtoToProduct(ProductDto productDto){
        Product product = new Product();

        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCostPrice(productDto.getCostPrice());
        product.setSalePrice(productDto.getSalePrice());
        product.setImage(productDto.getImage());
        product.setCategory(productDto.getCategory());
        product.setSubCategory(productDto.getSubCategory());
        product.set_activated(productDto.isActivated());
        product.set_deleted(productDto.isDeleted());

        return product;
    }

    public static SubCategoryDto mapSubCategoryToSubCategoryDto(SubCategory subCategory){
        return new SubCategoryDto(
                subCategory.getId(),
                subCategory.getName()
        );
    }

    /*public static ReviewDto mapReviewToReviewDto(Review review){
        return new ReviewDto(
                review.getReviewId(),
                review.getRatingNumber(),
                review.getFeedback(),
                review.getProductId(),
                review.getCustomer().getId(),
                review.getCustomer().getFirstName() + " " + review.getCustomer().getLastName()
        );
    }*/

    public static CustomerResponseDto mapCustomerToCustomerResponseDto(Customer customer){
        return new CustomerResponseDto(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getUsername(),
                customer.getPhoneNumber()
        );
    }

    public static ShoppingCartDto mapShoppingCartToShoppingCartDto(ShoppingCart shoppingCart){
        return new ShoppingCartDto(
                shoppingCart.getId(),
                shoppingCart.getTotalPrice(),
                shoppingCart.getTotalItems(),
                shoppingCart.getCartItems().stream().map(Mapper::mapCartItemToCartItemDto).collect(Collectors.toSet())
        );
    }

    public static CartItemDto mapCartItemToCartItemDto(CartItem cartItem){
        return new CartItemDto(
                cartItem.getId(),
                mapShoppingCartToShoppingCartDto(cartItem.getCart()),
                productToProductDto(cartItem.getProduct()),
                cartItem.getQuantity(),
                cartItem.getUnitPrice()
        );
    }

    public static DeliveryPersonDto mapDeliveryPersonToDeliveryPerosnDto(DeliveryPerson deliveryPerson) {

        return new DeliveryPersonDto(deliveryPerson.getFirstName(), deliveryPerson.getLastName(), deliveryPerson.getPhoneNumber());
    }

    public static OrderDetailDto mapOrderDetailToOrderDetailDto(OrderDetail orderDetail){
        Product product = orderDetail.getProduct();

        return new OrderDetailDto(
                product.getId(),
                product.getName(),
                product.getImage(),
                product.getSalePrice(),
                orderDetail.getProductQuantity(),
                product.getSalePrice() * orderDetail.getProductQuantity()
        );
    }

    public static OrderDto mapOrderToOrderDto(Order order) {

        List<OrderDetailDto> orderDetailDtoList =
                order.getOrderDetailList().stream()
                        .map(Mapper::mapOrderDetailToOrderDetailDto)
                        .toList();

        DeliveryPersonDto deliveryPersonDto = null;
        if (order.getDeliveryPerson() != null) {
            deliveryPersonDto = mapDeliveryPersonToDeliveryPerosnDto(order.getDeliveryPerson());
        }

        return new OrderDto(
                order.getId(),
                order.getOrderDate(),
                order.getDeliveryDate(),
                order.getOrderStatus().toString(),
                order.getTotalPrice(),
                order.getQuantity(),
                order.isAccept(),
                order.isDelivered(),
                orderDetailDtoList,
                deliveryPersonDto
        );
    }

}
