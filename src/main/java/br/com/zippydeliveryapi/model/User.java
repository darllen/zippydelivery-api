package br.com.zippydeliveryapi.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import br.com.zippydeliveryapi.util.entity.BusinessEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "User")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BusinessEntity implements Serializable {

   @Serial
   private static final long serialVersionUID = -2660334839251150243L;

   public static final String ROLE_CUSTOMER = "CUSTOMER";      // Make purchases in the system
   public static final String ROLE_RESTAURANT = "RESTAURANT";  // READ, DELETE, WRITE, UPDATE
   public static final String ROLE_ADMIN = "ADMIN";            // READ, DELETE, WRITE, UPDATE
   public static final String ROLE_RIDER = "RIDER";      // READ, DELETE, WRITE, UPDATE

   @Column(nullable = false)
   private String username;

   @JsonIgnore
   @Column(nullable = false)
   private String password;

   @JsonIgnore
   @ElementCollection(fetch = FetchType.EAGER)
   private List<String> roles = new ArrayList<>();

}