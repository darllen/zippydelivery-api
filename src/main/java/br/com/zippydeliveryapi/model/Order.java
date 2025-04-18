package br.com.zippydeliveryapi.model;

import java.time.LocalDateTime;
import java.util.List;
import br.com.zippydeliveryapi.model.dto.request.OrderRequest;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import br.com.zippydeliveryapi.util.entity.BusinessEntity;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Order")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BusinessEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "promoCode_id")
    private PromoCode promoCode;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<OrderItem> orderItems;

    private LocalDateTime dateTime;

    private int paymentMethod;

    private int orderStatus;

    private int paymentStatus;

    private Double totalAmount;

    private Double deliveryFee;

    private Address deliveryAddress;

    public static Order fromRequest(OrderRequest request) {
        return Order.builder()
                .paymentMethod(request.getPaymentMethod().getCode())
                .orderStatus(request.getOrderStatus().getCode())
                .paymentStatus(StatusEnum.PENDING.getCode())
                .build();
    }
}