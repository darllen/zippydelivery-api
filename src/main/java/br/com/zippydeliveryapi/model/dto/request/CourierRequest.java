package br.com.zippydeliveryapi.model.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierRequest {

    @CPF
    private String doc;

    @Email
    private String email;

    private String name;
    private String password;
    private String birthDate;
    private String vehicle;
    private String plate;
    private String phone;
    private int status;

}
