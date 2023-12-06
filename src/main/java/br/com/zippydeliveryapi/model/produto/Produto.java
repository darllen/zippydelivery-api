package br.com.zippydeliveryapi.model.produto;

import org.hibernate.annotations.Where;
import br.com.zippydeliveryapi.model.categoria.CategoriaProduto;
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
@Table(name = "Produto")
@Where(clause = "habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto extends EntidadeAuditavel {

   @ManyToOne
   @JoinColumn(name = "categoria_id")
   private CategoriaProduto categoria;

   @ManyToOne
   @JoinColumn(name = "empresa_id")
   private Empresa empresa;

   @Column(nullable = false, length = 100)
   private String titulo;

   @Column(nullable = false)
   private String imagem;

   @Column
   private String descricao;

   @Column(nullable = false)
   private Double preco;

   @Column
   private Boolean disponibilidade;

}
