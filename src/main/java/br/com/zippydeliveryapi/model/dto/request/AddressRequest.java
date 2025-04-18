package br.com.zippydeliveryapi.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    private String street;
    private String number;
    private String district;
    private String city;
    private String state;
    private String zipCode;
    private String complement;
    private String description;
    private boolean defaultForDelivery;

}
