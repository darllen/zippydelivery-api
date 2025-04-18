package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long > {

    Optional<PromoCode> findByCode(String code);

}
