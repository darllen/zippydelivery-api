package br.com.zippydeliveryapi.model.pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
      
    @Query("SELECT p FROM Pedido p WHERE p.empresa.id = :idEmpresa")
    List<Pedido> findByidEmpresa(@Param("idEmpresa") Long idEmpresa);
    
}

