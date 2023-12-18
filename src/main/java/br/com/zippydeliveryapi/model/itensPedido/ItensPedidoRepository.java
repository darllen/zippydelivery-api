package br.com.zippydeliveryapi.model.itensPedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItensPedidoRepository extends JpaRepository<ItensPedido, Long>{
    
    @Query("SELECT i FROM ItensPedido i WHERE i.pedido.id = :idPedido")
    List<ItensPedido> findByidPedido(@Param("idPedido") Long idPedido);
    
}

