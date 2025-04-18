package br.com.zippydeliveryapi.model.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import br.com.zippydeliveryapi.model.Address;
import br.com.zippydeliveryapi.model.Customer;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "Name is required")
    @Length(max = 100, message = "Name must have at most {max} characters")
    private String name;

    @NotBlank(message = "CPF is required")
    @CPF(message = "Invalid CPF")
    private String doc;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private List<Long> addressIds;

    public Customer build() {
        return Customer.builder()
                .name(name)
                .email(email)
                .password(password)
                .doc(doc)
                .build();
    }

    public static CustomerRequest fromEntity(Customer customer) {
        List<Long> addressIds = customer.getAddress() != null
                ? customer.getAddress().stream().map(Address::getId).collect(Collectors.toList())
                : new ArrayList<>();

        return CustomerRequest.builder()
                .name(customer.getName())
                .doc(customer.getDoc())
                .email(customer.getEmail())
                .addressIds(addressIds)
                .build();
    }
}