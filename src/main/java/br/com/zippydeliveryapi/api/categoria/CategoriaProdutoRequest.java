package br.com.zippydeliveryapi.api.categoria;

import br.com.zippydeliveryapi.model.categoria.CategoriaProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProdutoRequest {

  private String descricao;

  private long empresa_id;

  public CategoriaProduto build() {
    return CategoriaProduto.builder()
        .descricao(descricao)
        .build();
  }
}
