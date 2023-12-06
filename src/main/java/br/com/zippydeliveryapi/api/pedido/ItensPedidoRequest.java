package br.com.zippydeliveryapi.api.pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItensPedidoRequest {

    private Long id_produto;
    private Long id_pedido;
    private Integer qtdProduto;
    private Double valorUnitario;
 
}

