package br.com.zippydeliveryapi.api.pedido;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    @NotNull
    private List<ItensPedidoRequest> itens;
    private Long id_cliente;
    private Long id_empresa;
    private LocalDateTime dataHora;
    private String formaPagamento;
    private String statusPedido;
    private String statusPagamento;
    private Double taxaEntrega;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;
    private String numeroEndereco;

}
