package br.com.zippydeliveryapi.model.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import br.com.zippydeliveryapi.util.enums.PaymentMethodEnum;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull
    private List<OrderItemRequest> orderItems;
    private Long customerId;
    private Long restaurantId;
    private String promoCode;
    private LocalDateTime createdDate;
    private PaymentMethodEnum paymentMethod;
    private StatusEnum orderStatus;
    private StatusEnum paymentStatus;
    private Long deliveryAddressId;

}
