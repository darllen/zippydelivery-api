package br.com.zippydeliveryapi.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

   private Long productCategoryId;
   private Long restaurantId;
   private String title;
   private String imageUrl;
   private String description;
   private Double price;
   private Boolean available;

}