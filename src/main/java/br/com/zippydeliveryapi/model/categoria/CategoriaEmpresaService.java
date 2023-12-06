package br.com.zippydeliveryapi.model.categoria;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaEmpresaService {

    @Autowired
    private CategoriaEmpresaRepository repository;

    @Transactional
    public CategoriaEmpresa save(CategoriaEmpresa categoriaEmpresa) {
        categoriaEmpresa.setHabilitado(Boolean.TRUE);
        categoriaEmpresa.setVersao(1L);
        categoriaEmpresa.setDataCriacao(LocalDate.now());
        return repository.save(categoriaEmpresa);
    }

    public List<CategoriaEmpresa> findAll() {
        return repository.findAll();
    }

    public CategoriaEmpresa findById(Long id) {
        return repository.findById(id).get();
    }

    @Transactional
    public void update(Long id, CategoriaEmpresa categoriaEmpresaAlterado) {

        CategoriaEmpresa categoriaEmpresa = repository.findById(id).get();

        categoriaEmpresa.setDescricao(categoriaEmpresaAlterado.getDescricao());
        categoriaEmpresa.setVersao(categoriaEmpresa.getVersao() + 1);
        
        repository.save(categoriaEmpresa);
    }

    @Transactional
    public void delete(Long id) {

        CategoriaEmpresa categoriaEmpresa = repository.findById(id).get();
        categoriaEmpresa.setHabilitado(Boolean.FALSE);
        categoriaEmpresa.setVersao(categoriaEmpresa.getVersao() + 1);

        repository.save(categoriaEmpresa);
    }

}
