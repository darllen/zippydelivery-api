package br.com.zippydeliveryapi.model.categoria;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaProdutoService {

    @Autowired
    private CategoriaProdutoRepository repository;

    @Transactional
    public CategoriaProduto save(CategoriaProduto categoriaProduto) {
        categoriaProduto.setHabilitado(Boolean.TRUE);
        categoriaProduto.setVersao(1L);
        categoriaProduto.setDataCriacao(LocalDate.now());
        return repository.save(categoriaProduto);
    }

    public List<CategoriaProduto> findAll() {
        return repository.findAll();
    }

    public CategoriaProduto findById(Long id) {
        return repository.findById(id).get();
    }

     public List<CategoriaProduto> findByIdEmpresa(Long id) {
        return repository.findByEmpresaId(id);
    }

    // public List<CategoriasProdutoEmpresaResponse> findCategoriasByIdEmpresa(Long id) { 
    //     List<CategoriaProduto> categorias = repository.findByEmpresaId(id);
    //    List<Produto> produtos =  produtoRepository.findByEmpresaId(id);
    //     return  
    // }

    @Transactional
    public void update(Long id, CategoriaProduto categoriaProdutoAlterado) {

        CategoriaProduto categoriaProduto = repository.findById(id).get();

        categoriaProduto.setDescricao(categoriaProdutoAlterado.getDescricao());
        categoriaProduto.setVersao(categoriaProduto.getVersao() + 1);
        
        repository.save(categoriaProduto);
    }

    @Transactional
    public void delete(Long id) {

        CategoriaProduto categoriaProduto = repository.findById(id).get();
        categoriaProduto.setHabilitado(Boolean.FALSE);
        categoriaProduto.setVersao(categoriaProduto.getVersao() + 1);

        repository.save(categoriaProduto);
    }

}
