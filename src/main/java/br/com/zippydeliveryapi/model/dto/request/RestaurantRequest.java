package br.com.zippydeliveryapi.model.dto.request;

import br.com.zippydeliveryapi.util.enums.PaymentMethodEnum;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequest {

    @Length(max = 500, message = "The name must have at most {max} characters")
    private String name;

    @Length(max = 20, message = "CNPJ out of range, too long")
    private String doc;

    @Email
    private String email;

    private Long categoryId;
    private AddressRequest address;
    private Integer deliveryTime;
    private Double deliveryFee;
    private String phone;
    private String profileImage;
    private String coverImage;
    private StatusEnum status;
    private String password;
    private Set<PaymentMethodEnum> acceptedPaymentMethods;
}