package br.com.zippydeliveryapi.api.cupom;

import java.util.List;
import javax.validation.Valid;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import br.com.zippydeliveryapi.model.cupom.CupomDesconto;
import br.com.zippydeliveryapi.model.cupom.CupomDescontoService;

@RestController
@RequestMapping("/api/cupom")
@CrossOrigin
public class CupomDescontoController {

    @Autowired
    private CupomDescontoService cupomDescontoService;
    

    @ApiOperation(value = "Salvar um cupom de desconto no sistema.")
    @PostMapping
    public ResponseEntity<CupomDesconto> save(@RequestBody @Valid CupomDescontoRequest request) {
        CupomDesconto cupom = cupomDescontoService.save(request.build());
        return new ResponseEntity<CupomDesconto>(cupom, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Listar todos os cupons de desconto do sistema.")
    @GetMapping
    public List<CupomDesconto> findAll() {
        return cupomDescontoService.findAll();
    }

    @GetMapping("/{id}")
    public CupomDesconto findById(@PathVariable Long id) {
        return cupomDescontoService.findById(id);
    }

    @GetMapping("/codigo/{codigo}")
    public CupomDesconto findByCodigo(@PathVariable String codigo) {
        return cupomDescontoService.findByCodigo(codigo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CupomDesconto> update(@PathVariable("id") Long id, @RequestBody CupomDesconto request) {
        cupomDescontoService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Deletar um cupom de desconto do sistema por id.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Cupom deletado"),
        @ApiResponse(code = 401, message = "Acesso não autorizado"),
        @ApiResponse(code = 403, message = "Sem permissão"),
        @ApiResponse(code = 404, message = "Registro não encontrado"),
        @ApiResponse(code = 500, message = "Erro no servidor"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        cupomDescontoService.delete(id);
        return ResponseEntity.ok().build();
    }

    
}
