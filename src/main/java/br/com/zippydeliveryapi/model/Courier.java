package br.com.zippydeliveryapi.model;

import br.com.zippydeliveryapi.model.dto.request.CourierRequest;
import br.com.zippydeliveryapi.util.enums.StatusEnum;

import br.com.zippydeliveryapi.util.entity.BusinessEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Rider")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Courier extends BusinessEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    private String doc;

    @Email
    private String email;

    private String password;

    private String birthDate;

    private String vehicle;

    @Column(nullable = false, unique = true)
    private String plate;

    private String phone;

    private int status;

    public static Courier fromRequest(CourierRequest request) {
        return Courier.builder()
                .name(request.getName())
                .doc(request.getDoc())
                .email(request.getEmail())
                .birthDate(request.getBirthDate())
                .phone(request.getPhone())
                .vehicle(request.getVehicle())
                .plate(request.getPlate())
                .password(request.getPassword())
                .status(StatusEnum.PENDING.getCode())
                .build();
    }
}