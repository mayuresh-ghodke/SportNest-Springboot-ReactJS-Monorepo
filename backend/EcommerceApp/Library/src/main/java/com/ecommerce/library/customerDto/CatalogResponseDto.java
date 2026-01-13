package com.ecommerce.library.customerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogResponseDto {
    private List<CategoryWithSubCategoryResponseDto> categoryWithSubCategoryResponseDtoList;
    private List<ProductResponseDto> productResponseDtoList;
}
