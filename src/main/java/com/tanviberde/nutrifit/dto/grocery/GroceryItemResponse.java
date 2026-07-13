package com.tanviberde.nutrifit.dto.grocery;

import com.tanviberde.nutrifit.entity.GroceryItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroceryItemResponse {

    private Long id;
    private String itemName;
    private Double quantity;
    private String unit;
    private boolean purchased;

    public static GroceryItemResponse fromEntity(GroceryItem item) {
        return GroceryItemResponse.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .purchased(item.isPurchased())
                .build();
    }
}