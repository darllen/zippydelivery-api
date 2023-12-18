package br.com.zippydeliveryapi.model.categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {
    
    List<CategoriaProduto> findByEmpresaId(Long idEmpresa);
}
