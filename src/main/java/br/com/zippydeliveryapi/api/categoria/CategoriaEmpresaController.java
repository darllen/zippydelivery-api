package br.com.zippydeliveryapi.api.categoria;

import java.util.List;

import javax.validation.Valid;

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

import br.com.zippydeliveryapi.model.categoria.CategoriaEmpresa;
import br.com.zippydeliveryapi.model.categoria.CategoriaEmpresaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/api/categoriaempresa")
@CrossOrigin
public class CategoriaEmpresaController {

    @Autowired
    private CategoriaEmpresaService categoriaEmpresaService;


    @ApiOperation(value = "Serviço responsável por salvar uma categoria de empresa no sistema.")
    @PostMapping
    public ResponseEntity<CategoriaEmpresa> save(@RequestBody @Valid CategoriaEmpresaRequest request) {
        CategoriaEmpresa categoriaEmpresa = request.build();
        CategoriaEmpresa categoriaEmpresaNova = categoriaEmpresaService.save(categoriaEmpresa);

        return new ResponseEntity<CategoriaEmpresa>(categoriaEmpresaNova, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Serviço responsável por listar todas as categorias de empresa do sistema.")
    @GetMapping
    public List<CategoriaEmpresa> findAll() {
        return categoriaEmpresaService.findAll();
    }

    @ApiOperation(value = "Serviço responsável por obter uma categoria de empresa referente ao Id passado na URL.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna  a categoria de empresa."),
            @ApiResponse(code = 401, message = "Acesso não autorizado."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 404, message = "Não foi encontrado um registro para o Id informado."),
            @ApiResponse(code = 500, message = "Foi gerado um erro no servidor."),
    })
    
    @GetMapping("/{id}")
    public CategoriaEmpresa findById(@PathVariable Long id) {
        return categoriaEmpresaService.findById(id);
    }

    @ApiOperation(value = "Serviço responsável por atualizar uma categoria de empresa referente ao Id passado na URL.")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaEmpresa> update(@PathVariable("id") Long id, @RequestBody CategoriaEmpresaRequest request) {
        categoriaEmpresaService.update(id, request.build());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Serviço responsável por deletar uma categoria de empresa referente ao Id passado na URL.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaEmpresaService.delete(id);
        return ResponseEntity.ok().build();
    }

    


}