package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.model.dto.request.ProductRequest;
import br.com.zippydeliveryapi.util.entity.BusinessEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Product")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BusinessEntity {

    @ManyToOne
    @JoinColumn(name = "productCategory_id")
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String imageUrl;

    private String description;

    @Column(nullable = false)
    private Double price;

    private Boolean available;


    public static Product fromRequest(ProductRequest request) {
        return Product.builder()
                .title(request.getTitle())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .price(request.getPrice())
                .available(request.getAvailable())
                .build();
    }
}