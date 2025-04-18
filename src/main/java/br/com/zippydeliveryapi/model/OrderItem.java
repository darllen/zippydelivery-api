package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.util.entity.BusinessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "OrderItem")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BusinessEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Order order;

    private Integer quantity;

    private Double unitPrice;

    private Double totalPrice;
}

