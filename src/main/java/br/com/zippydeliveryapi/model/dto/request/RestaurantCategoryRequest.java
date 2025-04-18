package br.com.zippydeliveryapi.model.dto.request;

import br.com.zippydeliveryapi.model.RestaurantCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCategoryRequest {

    private String description;

    public RestaurantCategory build() {
        return RestaurantCategory.builder()
                .description(description)
                .build();
    }
}
