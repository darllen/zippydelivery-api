package br.com.zippydeliveryapi.model.pedido;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.zippydeliveryapi.api.pedido.DashBoardResponse;
import br.com.zippydeliveryapi.model.cupom.CupomDesconto;
import br.com.zippydeliveryapi.model.cupom.CupomDescontoService;
import br.com.zippydeliveryapi.model.itensPedido.ItensPedido;
import br.com.zippydeliveryapi.model.itensPedido.ItensPedidoRepository;
import br.com.zippydeliveryapi.model.mensagens.EmailService;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ItensPedidoRepository itensPedidoRepository;

    @Autowired
    private CupomDescontoService cupomDescontoService;
    
    @Autowired
    private EmailService emailService;


    private List<ItensPedido> criaListaPedidos(Pedido pedido) {
        List<ItensPedido> itens = new ArrayList<ItensPedido>();

        for (ItensPedido item : pedido.getItensPedido()) {
            itens.add(item);
        }
        return itens;
    }

    // SOMAR A TAXA
    private Double calcularValorTotalPedido(List<ItensPedido> itensPedidos) {
        Double valorTotal = 0.0;

        for (ItensPedido itens : itensPedidos) {
            valorTotal += itens.getValorTotal();
        }
        return valorTotal;
    }
    
    private Pedido salvarPedido(Pedido pedido, List<ItensPedido> itens) {
        pedido.setItensPedido(null);
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatusPagamento("Em aberto");
        pedido.setValorTotal(calcularValorTotalPedido(itens));
        pedido.setHabilitado(true);
    
        Pedido pedidoSalvo = repository.saveAndFlush(pedido);
    
        for (ItensPedido item : itens) {
            item.setPedido(pedidoSalvo);
            item.setHabilitado(true);
            itensPedidoRepository.saveAndFlush(item);
        }
    
        pedidoSalvo.setItensPedido(itens);
        emailService.enviarEmailFinalizaçãoPedido(pedidoSalvo);
      
        return pedidoSalvo;
    }
    
    @Transactional
    public Pedido save(Pedido novoPedido) {
        List<ItensPedido> itens = criaListaPedidos(novoPedido);
        Pedido pedidoSalvo = salvarPedido(novoPedido, itens);
        CupomDesconto cupom = novoPedido.getCupomDesconto();
    
        if (cupom != null && cupomDescontoService.validarCupom(cupom)) {
            cupomDescontoService.aplicarCupom(pedidoSalvo, cupom);
        }
    
        return pedidoSalvo;
    }  

    public List<Pedido> findAll() {
        return repository.findAll();
    }

    public Pedido findById(Long id) {
        return repository.findById(id).get();
    }

    
    public List<Pedido> findByIdEmpresa(Long id) {
        return repository.findByidEmpresa(id);
    }

    @Transactional
    public void update(Long id, String statusPagamento, String statusPedido) {
        Pedido pedido = repository.findById(id).get();
        pedido.setStatusPedido(statusPedido);
        pedido.setStatusPagamento(statusPagamento);
        pedido.setVersao(pedido.getVersao() + 1);

        repository.save(pedido);
    }

    @Transactional
    public void delete(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido", id));

        pedido.setHabilitado(Boolean.FALSE);
        pedido.setVersao(pedido.getVersao() + 1);

        repository.save(pedido);
    }

    public DashBoardResponse Dashboard(Long id) {
        List<Pedido> pedidos = repository.findByidEmpresa(id);
        DashBoardResponse response = new DashBoardResponse();
        Double vendasHojeValor = 0.0;

        response.setVendasTotais(pedidos.size());
        response.setFatoramentoTotal(0.0);
        response.setVendaHoje(0);

        for (Pedido pedido : pedidos) {
            response.setFatoramentoTotal(response.getFatoramentoTotal() + pedido.getValorTotal());
            if (pedido.getDataHora().toLocalDate().equals(LocalDate.now())) {
                response.setVendaHoje(response.getVendaHoje() + 1);
                vendasHojeValor = vendasHojeValor + pedido.getValorTotal();
            }
        }
        response.setFaturamentoMedio(vendasHojeValor / response.getVendaHoje());

        return response;
    }

    public List<DashBoardResponse> DashboardMensal(Long id) {
        List<Pedido> pedidos = repository.findByidEmpresa(id);
        List<DashBoardResponse> responses = new ArrayList<>();

        for (Integer i = 0; i < 12; i++) {
            responses.add(new DashBoardResponse());
        }
        for (Pedido pedido : pedidos) {
            Integer mes = pedido.getDataHora().getMonthValue() - 1;
            DashBoardResponse response = responses.get(mes);

            if (response.getVendasTotais() == null) {
                response.setVendasTotais(0);
            }
            response.setVendasTotais(response.getVendasTotais() + 1);
            if (response.getFatoramentoTotal() == null) {
                response.setFatoramentoTotal(0.0);
            }
            response.setFatoramentoTotal(response.getFatoramentoTotal() + pedido.getValorTotal());
        }
        for (DashBoardResponse dashBoardResponse : responses) {
            if (dashBoardResponse.getVendasTotais() == null) {
                dashBoardResponse.setVendasTotais(0);
            }
            if (dashBoardResponse.getFatoramentoTotal() == null) {
                dashBoardResponse.setFatoramentoTotal(0.0);
            }
            if (dashBoardResponse.getVendasTotais() > 0) {
                dashBoardResponse.setFaturamentoMedio(dashBoardResponse.getFatoramentoTotal() / dashBoardResponse.getVendasTotais());
            }
        }
        return responses;
    }

    public DashBoardResponse DashboardAll() {
        List<Pedido> pedidos = repository.findAll();
        DashBoardResponse response = new DashBoardResponse();
        Double vendasHojeValor = 0.0;

        response.setVendasTotais(pedidos.size());
        response.setFatoramentoTotal(0.0);
        response.setVendaHoje(0);

        for (Pedido pedido : pedidos) {
            response.setFatoramentoTotal(response.getFatoramentoTotal() + pedido.getValorTotal());
            if (pedido.getDataHora().toLocalDate().equals(LocalDate.now())) {
                response.setVendaHoje(response.getVendaHoje() + 1);
                vendasHojeValor = vendasHojeValor + pedido.getValorTotal();
            }
        }
        response.setFaturamentoMedio(vendasHojeValor / response.getVendaHoje());

        return response;
    }

    public List<DashBoardResponse> DashboardMensalAll() {
        List<Pedido> pedidos = repository.findAll();
        List<DashBoardResponse> responses = new ArrayList<>();

        for (Integer i = 0; i < 12; i++) {
            responses.add(new DashBoardResponse());
        }
        for (Pedido pedido : pedidos) {
            Integer mes = pedido.getDataHora().getMonthValue() - 1;
            DashBoardResponse response = responses.get(mes);

            if (response.getVendasTotais() == null) {
                response.setVendasTotais(0);
            }
            response.setVendasTotais(response.getVendasTotais() + 1);

            if (response.getFatoramentoTotal() == null) {
                response.setFatoramentoTotal(0.0);
            }
            response.setFatoramentoTotal(response.getFatoramentoTotal() + pedido.getValorTotal());
        }
        for (DashBoardResponse dashBoardResponse : responses) {
            if (dashBoardResponse.getVendasTotais() == null) {
                dashBoardResponse.setVendasTotais(0);
            }
            if (dashBoardResponse.getFatoramentoTotal() == null) {
                dashBoardResponse.setFatoramentoTotal(0.0);
            }
            if (dashBoardResponse.getVendasTotais() > 0) {
                dashBoardResponse.setFaturamentoMedio(dashBoardResponse.getFatoramentoTotal() / dashBoardResponse.getVendasTotais());
            }
        }
        return responses;
    }

}
