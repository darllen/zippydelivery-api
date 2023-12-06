package br.com.zippydeliveryapi.model.itensPedido;

import org.hibernate.annotations.Where;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.zippydeliveryapi.model.pedido.Pedido;
import br.com.zippydeliveryapi.model.produto.Produto;
import br.com.zippydeliveryapi.util.entity.EntidadeAuditavel;

import java.io.Serializable;
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
@Table(name = "ItensPedido")
@Where(clause = "habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItensPedido extends EntidadeAuditavel implements Serializable {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Produto produto;
  
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Pedido pedido;

    @Column
    private Integer qtdProduto;

    @Column
    private Double valorUnitario;

    @Column
    private Double valorTotal;

}

