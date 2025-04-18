package br.com.zippydeliveryapi.model.dto.response;

import br.com.zippydeliveryapi.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantProductCategoryResponse {

    private String title;
    private List<Product> products;

}
