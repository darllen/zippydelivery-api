package br.com.zippydeliveryapi.model.empresa;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import org.hibernate.annotations.Where;
import br.com.zippydeliveryapi.api.empresa.EmpresaRequest;
import br.com.zippydeliveryapi.model.acesso.Usuario;
import br.com.zippydeliveryapi.model.categoria.CategoriaEmpresa;
import br.com.zippydeliveryapi.util.entity.EntidadeAuditavel;
import br.com.zippydeliveryapi.util.enums.FormaPagamento;
import lombok.*;

@Entity
@Table(name = "Empresa")
@Where(clause = "habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Empresa extends EntidadeAuditavel {

  @ManyToOne
  @JoinColumn(nullable = false)
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "idCategoria")
  private CategoriaEmpresa categoria;

  @ElementCollection(targetClass = FormaPagamento.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "empresa_formas_pagamento", joinColumns = @JoinColumn(name = "empresa_id"))
  @Column(name = "forma_pagamento", nullable = false)
  @Enumerated(EnumType.STRING)
  private Set<FormaPagamento> formasPagamento;

  // @Column(nullable = false, length = 100)
  private String nome;

  // @Column(unique = true)
  private String cnpj;

  @Column(nullable = false, unique = true)
  private String email;

  @Column
  private Integer tempoEntrega;

  @Column
  private Double taxaFrete;

  @Column
  private String telefone;

  @Column
  private String imgPerfil;

  @Column
  private String imgCapa;

  @Column
  private String logradouro;

  @Column
  private String bairro;

  @Column
  private String cidade;

  @Column
  private String estado;

  // @Column(nullable = false, length = 10)
  private String cep;

  @Column
  private String complemento;

  @Column
  private String numeroEndereco;

  @Column
  private String status;

  public static Empresa fromRequest(EmpresaRequest request, CategoriaEmpresa categoria) {
    Usuario usuario = Usuario.builder()
        .roles(Arrays.asList(Usuario.ROLE_EMPRESA))
        .username(request.getEmail())
        .password(request.getSenha())
        .build();

    Set<FormaPagamento> formasPagamento = Arrays.stream(request.getFormasPagamento())
        .map(FormaPagamento::valueOf)
        .collect(Collectors.toSet());

    return Empresa.builder()
        .nome(request.getNome())
        .cnpj(request.getCnpj())
        .email(request.getEmail())
        .usuario(usuario)
        .categoria(categoria)
        .tempoEntrega(request.getTempoEntrega())
        .taxaFrete(request.getTaxaFrete())
        .telefone(request.getTelefone())
        .imgPerfil(request.getImgPerfil())
        .imgCapa(request.getImgCapa())
        .logradouro(request.getLogradouro())
        .bairro(request.getBairro())
        .cidade(request.getCidade())
        .estado(request.getEstado())
        .cep(request.getCep())
        .complemento(request.getComplemento())
        .numeroEndereco(request.getNumeroEndereco())
        .formasPagamento(formasPagamento)
        .build();
  }


}
