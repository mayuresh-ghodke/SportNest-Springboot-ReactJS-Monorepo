package com.ecommerce.library.customerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWithSubCategoryResponseDto {

    private Long id;
    private String categoryName;
    private List<SubCategoryResponseDto> subCategoryResponseDtoList;
}



