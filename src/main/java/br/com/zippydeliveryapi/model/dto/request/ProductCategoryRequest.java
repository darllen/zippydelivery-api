package br.com.zippydeliveryapi.model.dto.request;

import br.com.zippydeliveryapi.model.ProductCategory;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryRequest {

  @Column(name = "restaurant_id")
  private Long restaurantId;

  private String description;

  public ProductCategory build() {
    return ProductCategory.builder()
        .description(description)
        .build();
  }
}
