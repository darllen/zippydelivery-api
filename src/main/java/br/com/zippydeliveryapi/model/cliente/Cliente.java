package br.com.zippydeliveryapi.model.cliente;

import org.hibernate.annotations.Where;

import br.com.zippydeliveryapi.model.acesso.Usuario;
import br.com.zippydeliveryapi.util.entity.EntidadeAuditavel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Cliente")
@Where(clause = "habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente extends EntidadeAuditavel {


  @ManyToOne
   @JoinColumn(nullable = false)
   private Usuario usuario;

  //  @Column(nullable = false, length = 100)
    private String nome;

    //@Column(unique = false)
    private String cpf;

   // @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String senha;

    @Column
    private String logradouro;

    @Column
    private String bairro;

    @Column
    private String cidade;

    @Column
    private String estado;

    @Column
    private String cep;

    @Column
    private String complemento;

}
