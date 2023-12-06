package br.com.zippydeliveryapi.model.categoria;

import java.util.List;

import br.com.zippydeliveryapi.model.produto.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriasProdutoEmpresaResponse {

    private String titulo;
    private List<Produto> produtos;

}
