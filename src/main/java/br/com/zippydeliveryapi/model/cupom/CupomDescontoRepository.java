package br.com.zippydeliveryapi.model.cupom;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long >{
    Optional<CupomDesconto> findByCodigo(String codigo);
}
