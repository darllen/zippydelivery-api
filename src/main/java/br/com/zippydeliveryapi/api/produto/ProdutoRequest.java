package br.com.zippydeliveryapi.api.produto;

import br.com.zippydeliveryapi.model.produto.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest {

   private Long idCategoria;
   private String titulo;
   private String imagem;
   private String descricao;
   private Double preco;
   private Boolean disponibilidade;
   private Integer tempoEntregaMinimo;
   private Integer tempoEntregaMaximo;

   public Produto build() {
      return Produto.builder()
            .descricao(descricao)
            .imagem(imagem)
            .titulo(titulo)
            .disponibilidade(disponibilidade)
            .preco(preco)
            .build();
   }
}
