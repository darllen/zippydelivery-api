package br.com.zippydeliveryapi.model;

import java.util.Set;
import br.com.zippydeliveryapi.model.dto.request.RestaurantRequest;
import br.com.zippydeliveryapi.util.entity.BusinessEntity;
import br.com.zippydeliveryapi.util.enums.PaymentMethodEnum;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "Restaurant")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant extends BusinessEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private RestaurantCategory category;

    @ElementCollection(targetClass = PaymentMethodEnum.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "restaurant_payment_methods", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private Set<PaymentMethodEnum> acceptedPaymentMethods;

    private String name;

    private String doc;

    @Email
    private String email;

    private Integer deliveryTime;

    private Double deliveryFee;

    private String phone;

    private String profileImage;

    private String coverImage;

    private int status;

    private String password;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public static Restaurant fromRequest(RestaurantRequest request) {
        return Restaurant.builder()
                .acceptedPaymentMethods(request.getAcceptedPaymentMethods())
                .name(request.getName())
                .doc(request.getDoc())
                .email(request.getEmail())
                .deliveryTime(request.getDeliveryTime())
                .deliveryFee(request.getDeliveryFee())
                .phone(request.getPhone())
                .profileImage(request.getProfileImage())
                .coverImage(request.getCoverImage())
                .status(StatusEnum.PENDING.getCode())
                .build();
    }
}