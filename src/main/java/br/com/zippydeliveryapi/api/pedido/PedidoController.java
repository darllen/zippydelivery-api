package br.com.zippydeliveryapi.api.pedido;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zippydeliveryapi.model.cliente.ClienteService;
import br.com.zippydeliveryapi.model.empresa.Empresa;
import br.com.zippydeliveryapi.model.empresa.EmpresaService;
import br.com.zippydeliveryapi.model.itensPedido.ItensPedido;
import br.com.zippydeliveryapi.model.pedido.PedidoService;
import br.com.zippydeliveryapi.model.produto.Produto;
import br.com.zippydeliveryapi.model.produto.ProdutoService;
import br.com.zippydeliveryapi.model.pedido.Pedido;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/pedido")
@CrossOrigin
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ProdutoService produtoService;

    
    @PostMapping
    public ResponseEntity<Pedido> save(@RequestBody @Valid PedidoRequest request) {
        Empresa empresa = empresaService.findById(request.getId_empresa());

        Double valorTotal = 0.0;
        
        Pedido pedidoNovo = Pedido.builder()
                .empresa(empresa)
                .dataHora(request.getDataHora())
                .formaPagamento(request.getFormaPagamento())
                .statusPagamento(request.getStatusPagamento())
                .statusPedido(request.getStatusPedido())
                .taxaEntrega(request.getTaxaEntrega())
                .logradouro(request.getLogradouro())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .estado(request.getEstado())
                .cep(request.getCep())
                .complemento(request.getComplemento())
                .numeroEndereco(request.getNumeroEndereco())
                .cliente(clienteService.findById(request.getId_cliente()))
                .itensPedido(criarListaItensPedidos(request.getItens()))
                .build();

        for (ItensPedido itens: pedidoNovo.getItensPedido()) {
            valorTotal = valorTotal + itens.getValorTotal();
        }

        pedidoNovo.setValorTotal(valorTotal);

        Pedido pedido = pedidoService.save(pedidoNovo);

        return new ResponseEntity<Pedido>(pedido, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Pedido> findAll() {
        return pedidoService.findAll();
    }

    @GetMapping("/{id}")
    public Pedido findById(@PathVariable Long id) {
        return pedidoService.findById(id);
    }
    
    @GetMapping("/findByEmpresa/{id}")
    public List<Pedido> findByIdEmpresa(@PathVariable Long id) {
        return pedidoService.findByIdEmpresa(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable("id") Long id, @RequestBody PedidoRequest request) {
        pedidoService.update(id, request.getStatusPagamento(), request.getStatusPedido());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.delete(id);
        return ResponseEntity.ok().build();
    }

    private List<ItensPedido> criarListaItensPedidos(List<ItensPedidoRequest> requestItensPedido) {
        List<ItensPedido> itens = new ArrayList<ItensPedido>();

        for (ItensPedidoRequest itensPedidoRequest : requestItensPedido) {
            Produto produto = produtoService.findById(itensPedidoRequest.getId_produto());
            ItensPedido item = ItensPedido.builder()
                    .produto(produto)
                    .qtdProduto(itensPedidoRequest.getQtdProduto())
                    .valorUnitario(itensPedidoRequest.getValorUnitario())
                    .valorTotal(itensPedidoRequest.getValorUnitario() * itensPedidoRequest.getQtdProduto())
                    .build();
            itens.add(item);
        }
        return itens;
    }

    @GetMapping("/dashboard/{id}")
    public DashBoardResponse Dashboard(@PathVariable Long id) {
        return pedidoService.Dashboard(id);
    }

    @GetMapping("/dashboardMensal/{id}")
    public List<DashBoardResponse> DashboardMensal(@PathVariable Long id) {
        return pedidoService.DashboardMensal(id);
    }

    @GetMapping("/dashboardAll")
    public DashBoardResponse DashboardAll() {
        return pedidoService.DashboardAll();
    }

    @GetMapping("/dashboardMensalAll")
    public List<DashBoardResponse> DashboardMensalAll() {
        return pedidoService.DashboardMensalAll();
    }

}
