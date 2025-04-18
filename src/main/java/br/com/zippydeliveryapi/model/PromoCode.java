package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.model.dto.request.PromoCodeRequest;
import br.com.zippydeliveryapi.util.entity.BusinessEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "PromoCode")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromoCode extends BusinessEntity {

    @Column(unique = true)
    private String code;

    private Double discountPercentage;

    private Double discountValue;

    private Double minAllowedOrderValue;

    private Integer maxUsageQuantity;

    private LocalDate startDate;

    private LocalDate endDate;

    public static PromoCode fromRequest(PromoCodeRequest request) {
        return PromoCode.builder()
                .code(request.getCode())
                .discountPercentage(request.getDiscountPercentage())
                .discountValue(request.getDiscountValue())
                .minAllowedOrderValue(request.getMinOrderValue())
                .maxUsageQuantity(request.getMaxUsageQuantity())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }
}