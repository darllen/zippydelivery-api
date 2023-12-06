package br.com.zippydeliveryapi.model.categoria;

import org.hibernate.annotations.Where;

import br.com.zippydeliveryapi.model.empresa.Empresa;
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
@Table(name = "CategoriaProduto")
@Where(clause = "habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaProduto extends EntidadeAuditavel {

    @Column
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    
}
