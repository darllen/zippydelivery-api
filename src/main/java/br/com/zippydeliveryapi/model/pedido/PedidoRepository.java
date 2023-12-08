package br.com.zippydeliveryapi.model.pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
      
    @Query("SELECT p FROM Pedido p WHERE p.empresa.id = :idEmpresa")
    List<Pedido> findByidEmpresa(@Param("idEmpresa") Long idEmpresa);    
    
    
    @Query(value = "SELECT p FROM Pedido p WHERE p.cliente.id = :idCliente")
    List<Pedido> filtrarPedidosPorCliente(Long idCliente);

}

