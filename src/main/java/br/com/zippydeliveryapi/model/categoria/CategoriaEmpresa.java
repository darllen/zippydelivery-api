package br.com.zippydeliveryapi.model.categoria;

import org.hibernate.annotations.Where;

import br.com.zippydeliveryapi.util.entity.EntidadeAuditavel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "CategoriaEmpresa")
@Where(clause = "habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaEmpresa extends EntidadeAuditavel {

    @Column
    private String descricao;
}