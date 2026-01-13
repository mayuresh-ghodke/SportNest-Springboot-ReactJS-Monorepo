package com.ecommerce.library.collectionMapper;

import com.ecommerce.library.customerDto.*;
import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.OrderDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.dto.SubCategoryDto;
import com.ecommerce.library.mapper.ResponseDtoMapper;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionMapper {

    private CollectionMapper(){}

    public static List<CategoryResponseDto> mapCategoryDtoListToCategoryResponseDtoList(List<CategoryDto> categoryDtos){
        return categoryDtos.stream().map(ResponseDtoMapper::mapCategoryDtoToCategoryResponseDto).toList();
    }

    public static List<ProductResponseDto> mapProductDtoListToProductResponseDtoList(List<ProductDto> productDtos){
        return productDtos.stream().map(ResponseDtoMapper::mapProductDtoToProductResponseDto).toList();
    }

    public static List<SubCategoryResponseDto> mapSubCategoryListDtoToSubCategoryResponseDtoList(List<SubCategoryDto> subCategoryDtos){
        return subCategoryDtos.stream().map(ResponseDtoMapper::mapSubCategoryDtoToSubCategoryResponseDto).toList();
    }

    public static List<ReviewResponseDto> mapReviewListToReviewResponseDto(List<Review> reviews){
        return reviews.stream().map(ResponseDtoMapper::mapReviewToReviewResponseDto).toList();
    }

    public static List<OrderResponseDto> mapOrderDtoListToOrderResponseDto(List<OrderDto> orders){
        return orders.stream().map(ResponseDtoMapper::mapOrderDtoToOrderResponseDto).toList();
    }
}
