package br.com.zippydeliveryapi.model.dto.request;

import java.time.LocalDate;
import br.com.zippydeliveryapi.model.PromoCode;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeRequest {

  @NotBlank(message = "The promo code is required")
  @Length(max = 10, message = "The promo code must have at most {max} characters")
  private String code;

  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDate startDate;

  @JsonFormat(pattern = "dd/MM/yyyy")
  @Future(message = "The end date must be in the future")
  private LocalDate endDate;

  private Double discountValue;
  private Double discountPercentage;
  private Double minOrderValue;
  private Integer maxUsageQuantity;

  public PromoCode build() {
    return PromoCode.builder()
            .code(code)
            .discountPercentage(discountPercentage)
            .discountValue(discountValue)
            .minAllowedOrderValue(minOrderValue)
            .maxUsageQuantity(maxUsageQuantity)
            .startDate(startDate)
            .endDate(endDate)
            .build();
  }

  public static PromoCodeRequest fromEntity(PromoCode promoCode) {
    PromoCodeRequest request = new PromoCodeRequest();
    request.setCode(promoCode.getCode());
    request.setDiscountPercentage(promoCode.getDiscountPercentage());
    request.setDiscountValue(promoCode.getDiscountValue());
    request.setMinOrderValue(promoCode.getMinAllowedOrderValue());
    request.setMaxUsageQuantity(promoCode.getMaxUsageQuantity());
    request.setStartDate(promoCode.getStartDate());
    request.setEndDate(promoCode.getEndDate());
    return request;
  }
}