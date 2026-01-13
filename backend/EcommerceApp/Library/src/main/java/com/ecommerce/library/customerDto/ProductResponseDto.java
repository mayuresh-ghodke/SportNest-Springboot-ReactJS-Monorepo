package com.ecommerce.library.customerDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

        private Long id;
        private String name;
        private String description;
        private int currentQuantity;
        private double costPrice;
        private String image;

        // Category info (flattened)
        private Long categoryId;
        private String categoryName;

        // SubCategory info (flattened)
        private Long subCategoryId;
        private String subCategoryName;

        private boolean activated;
        private boolean deleted;
}

