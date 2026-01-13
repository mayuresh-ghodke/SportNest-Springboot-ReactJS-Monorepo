package com.ecommerce.library.service.impl;

import com.ecommerce.library.customerDto.*;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.ShoppingCartDto;
import com.ecommerce.library.exception.AddressNotFoundException;
import com.ecommerce.library.exception.CartEmptyException;
import com.ecommerce.library.mapper.Mapper;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CheckoutService;
import com.ecommerce.library.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerService customerService;
    private final AddressService addressService;

    @Override
    public CheckoutResponseDto getCheckoutResponse(CustomerDto customer) {

        CustomerResponseDto customerResponseDto =
                ResponseDtoMapper.mapCustomerDtoToCustomerResponseDto(customer);

        Long customerId = customerService.getCustomerId(customer.getUsername());

        Address address = addressService.getAddressByCustomerId(customerId);
        if (address == null) {
            throw new AddressNotFoundException("Address not found");
        }
        ShoppingCart cart = customerService
                .findByUsername(customer.getUsername())
                .getCart();

        if (cart.getCartItems().isEmpty()) {
            throw new CartEmptyException("Cart is empty");
        }

        AddressResponseDto addressResponseDto = ResponseDtoMapper.mapAddressToAddressResponseDto(address);

        List<OrderDetailResponseDto> orderDetailResponseDtos =
                cart.getCartItems().stream()
                        .map(item -> new OrderDetailResponseDto(
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getProduct().getImage(),
                                item.getProduct().getSalePrice(),
                                item.getQuantity(),
                                item.getProduct().getSalePrice() * item.getQuantity()
                        ))
                        .toList();

        double subTotal = orderDetailResponseDtos.stream()
                .mapToDouble(OrderDetailResponseDto::getSubTotal)
                .sum();

        CheckoutResponseDto checkoutResponseDto = new CheckoutResponseDto();
        checkoutResponseDto.setCustomerResponseDto(customerResponseDto);
        checkoutResponseDto.setAddressResponseDto(addressResponseDto);
        checkoutResponseDto.setOrderDetailResponseDtoList(orderDetailResponseDtos);
        checkoutResponseDto.setSubTotal(subTotal);
        checkoutResponseDto.setGrandTotal(subTotal);

        return checkoutResponseDto;
    }
}
