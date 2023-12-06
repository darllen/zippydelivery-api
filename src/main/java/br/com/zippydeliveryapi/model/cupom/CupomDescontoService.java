package br.com.zippydeliveryapi.model.cupom;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zippydeliveryapi.model.pedido.Pedido;
import br.com.zippydeliveryapi.util.exception.CupomDescontoException;
import br.com.zippydeliveryapi.util.exception.EntidadeNaoEncontradaException;

@Service
public class CupomDescontoService { 

    @Autowired
    private CupomDescontoRepository repository;

    @Transactional
    public CupomDesconto save(CupomDesconto cupom) {
        validateDateRange(cupom);

        cupom.setHabilitado(Boolean.TRUE);
        cupom.setVersao(1L);
        cupom.setDataCriacao(LocalDate.now());
        return repository.save(cupom);
    }

    public List<CupomDesconto> findAll() {
        return repository.findAll();
    }

    public CupomDesconto findById(Long id) {
        Optional<CupomDesconto> consulta = repository.findById(id);

        if (consulta.isPresent()) {
            return consulta.get();
        } else {
            throw new EntidadeNaoEncontradaException("Cupom Desconto", id);
        }
    }

    @Transactional
    public CupomDesconto findByCodigo(String codigo) {
        Optional<CupomDesconto> consulta = repository.findByCodigo(codigo);

        if (consulta.isPresent()) {
            return consulta.get();
        } else {
            return null;
        }
    }

    @Transactional
    public void update(Long id, CupomDesconto cupomAlterado) {
        CupomDesconto cupom = repository.findById(id).get();

        cupom.setCodigo(cupomAlterado.getCodigo());
        cupom.setPercentualDesconto(cupomAlterado.getPercentualDesconto());
        cupom.setValorDesconto(cupomAlterado.getValorDesconto());
        cupom.setValorMinimoPedidoPermitido(cupomAlterado.getValorMinimoPedidoPermitido());
        cupom.setQuantidadeMaximaUso(cupomAlterado.getQuantidadeMaximaUso());
        cupom.setInicioVigencia(cupomAlterado.getInicioVigencia());
        cupom.setFimVigencia(cupomAlterado.getFimVigencia());

        cupom.setVersao(cupom.getVersao() + 1);
        repository.save(cupom);
    }

    @Transactional
    public void delete(Long id) {
        CupomDesconto cupom = repository.findById(id).get();
        cupom.setHabilitado(Boolean.FALSE);
        cupom.setVersao(cupom.getVersao() + 1);
        cupom.setCodigo("");
        repository.save(cupom);
    }

    public void validateDateRange(CupomDesconto cupom) {
        Objects.requireNonNull(cupom, "CupomDesconto não pode ser nulo");
        if (!cupom.getFimVigencia().isAfter(cupom.getInicioVigencia())) {
            throw new CupomDescontoException(CupomDescontoException.MESSAGE_DATA_INVALIDA);
        }
    }

    public boolean validarCupom(CupomDesconto cupom) {
        LocalDate date = LocalDate.now();
        LocalDate inicio = cupom.getInicioVigencia();
        LocalDate fim = cupom.getFimVigencia();
    
        return (date.isEqual(inicio) || date.isAfter(inicio)) && (date.isEqual(fim) || date.isBefore(fim));
    }

    public void aplicarDescontoNoPedido(Pedido pedido, Double desconto) {
        Double valorTotalComDesconto = pedido.getValorTotal() - desconto;
        pedido.setValorTotal(valorTotalComDesconto);
    }
    
    public void aplicarCupom(Pedido pedido, CupomDesconto cupom) {
        Double desconto = 0.0;
    
        if (cupom.getPercentualDesconto() != null && cupom.getPercentualDesconto() != 0.0) {
            desconto = pedido.getValorTotal() * (cupom.getPercentualDesconto() / 100);
        } else if (cupom.getValorDesconto() != null && cupom.getValorDesconto() != 0.0) {
            desconto = cupom.getValorDesconto();
        }
    
        aplicarDescontoNoPedido(pedido, desconto);
        cupom.setQuantidadeMaximaUso(cupom.getQuantidadeMaximaUso() - 1);
        this.update(cupom.getId(), cupom);
    }
}
