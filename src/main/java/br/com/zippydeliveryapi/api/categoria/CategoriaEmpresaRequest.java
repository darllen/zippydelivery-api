package br.com.zippydeliveryapi.api.categoria;

import br.com.zippydeliveryapi.model.categoria.CategoriaEmpresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaEmpresaRequest {

    private String descricao;

    public CategoriaEmpresa build() {
        return CategoriaEmpresa.builder()
                .descricao(descricao)
                .build();
    }
}