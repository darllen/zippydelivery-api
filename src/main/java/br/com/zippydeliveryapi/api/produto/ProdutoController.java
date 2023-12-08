package br.com.zippydeliveryapi.api.produto;

import java.util.List;
import javax.validation.Valid;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.zippydeliveryapi.model.categoria.CategoriaProdutoService;
import br.com.zippydeliveryapi.model.produto.Produto;
import br.com.zippydeliveryapi.model.produto.ProdutoService;


@RestController
@RequestMapping("/api/produto")
@CrossOrigin
public class ProdutoController {

    @Autowired
    private CategoriaProdutoService categoriaProdutoService;

    @Autowired
    private ProdutoService produtoService;

  
    @ApiOperation(value = "Serviço responsável por salvar um produto no sistema.")
    @PostMapping
    public ResponseEntity<Produto> save(@RequestBody @Valid ProdutoRequest request) {
        Produto produtoNovo = request.build();
        produtoNovo.setCategoria(categoriaProdutoService.findById(request.getIdCategoria()));
        Produto produto = produtoService.save(produtoNovo);

        return new ResponseEntity<Produto>(produto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Serviço responsável por listar todas os produto do sistema.")
    @GetMapping
    public List<Produto> findAll() {
        return produtoService.findAll();
    }

    @ApiOperation(value = "Serviço responsável por obter um produto referente ao Id passado na URL.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna  o produto."),
            @ApiResponse(code = 401, message = "Acesso não autorizado."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 404, message = "Não foi encontrado um registro para o Id informado."),
            @ApiResponse(code = 500, message = "Foi gerado um erro no servidor."),
    })

    @GetMapping("/{id}")
    public Produto findById(@PathVariable Long id) {
        return produtoService.findById(id);
    }


    @ApiOperation(value = "Serviço responsável por atualizar um produto referente ao Id passado na URL.")
    @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable("id") Long id, @RequestBody ProdutoRequest request) {
        Produto produto = request.build();
        produtoService.update(id, produto);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Serviço responsável por deletar um produto referente ao Id passado na URL.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produtoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Serviço responsável por listar produtos de uma mesma categoria.")
    @GetMapping("/porcategoria")
    public List<List<Object>> agruparPorCategoria() {
        return produtoService.agruparPorCategoria();
    }

     @GetMapping("/porcategoriaeempresa/{id}")
    public List<List<Object>> agruparPorCategoriaeEmpresa(@PathVariable Long id) {
        return produtoService.agruparPorCategoriaeEmpresa(id);
    }


}
