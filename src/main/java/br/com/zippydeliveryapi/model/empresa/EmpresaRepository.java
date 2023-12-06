package br.com.zippydeliveryapi.model.empresa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zippydeliveryapi.model.acesso.Usuario;


public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Empresa findByUsuario(Optional<Usuario> usuario);
}
