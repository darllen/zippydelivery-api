package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.util.entity.BusinessEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "RestaurantCategory")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCategory extends BusinessEntity {

    private String description;

}
