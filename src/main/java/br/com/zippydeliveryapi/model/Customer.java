package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.model.dto.request.CustomerRequest;
import br.com.zippydeliveryapi.util.entity.BusinessEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "Customer")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BusinessEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private String name;

    private String doc;

    private String email;

    private String password;

    private List<Address> address;

    public static Customer fromRequest(CustomerRequest request) {
        return Customer.builder()
                .name(request.getName())
                .doc(request.getDoc())
                .email(request.getEmail())
                .build();
    }
}