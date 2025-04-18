package br.com.zippydeliveryapi.util.entity;

import java.time.LocalDate;
import org.springframework.data.annotation.LastModifiedDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity extends BusinessEntity {
  
   @JsonIgnore
   private Long version;

   @JsonIgnore
   private LocalDate createdDate;

   @JsonIgnore
   @LastModifiedDate
   private LocalDate lastModifiedDate;

   @JsonIgnore
   private Long createdById;

   @JsonIgnore
   private Long lastModifiedById;

}
