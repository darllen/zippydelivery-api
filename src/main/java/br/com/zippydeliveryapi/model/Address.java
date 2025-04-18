package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.model.dto.request.AddressRequest;
import br.com.zippydeliveryapi.util.entity.BusinessEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import java.io.Serializable;

@Entity
@Table(name = "Address")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address extends BusinessEntity implements Serializable {

    private String street;

    private String number;

    private String district;

    private String city;

    private String state;

    @Length(max = 10, message = "ZIP code must have at most {max} characters")
    private String zipCode;

    private String complement;

    private String description;

    private boolean defaultForDelivery;

    public static Address fromRequest(AddressRequest request) {
        return Address.builder()
                .street(request.getStreet())
                .number(request.getNumber())
                .district(request.getDistrict())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .complement(request.getComplement())
                .description(request.getDescription())
                .defaultForDelivery(request.isDefaultForDelivery())
                .build();
    }
}