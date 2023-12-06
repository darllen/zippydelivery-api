package br.com.zippydeliveryapi.api.empresa;


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
import br.com.zippydeliveryapi.model.cliente.Cliente;
import br.com.zippydeliveryapi.model.empresa.Empresa;
import br.com.zippydeliveryapi.model.empresa.EmpresaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/empresa")
@CrossOrigin
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private CategoriaEmpresaService categoriaEmpresaService;

    @ApiOperation(value = "Serviço responsável por salvar uma empresa no sistema.")
    @PostMapping
    public ResponseEntity<Empresa> save(@RequestBody @Valid EmpresaRequest request) {

        CategoriaEmpresa categoria = categoriaEmpresaService.findById(request.getIdCategoria());
        Empresa empresaNova = Empresa.fromRequest(request, categoria);
        Empresa empresaCriada = empresaService.save(empresaNova);
        
        return new ResponseEntity<Empresa>(empresaCriada, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Serviço responsável por listar todas as empresas do sistema.")
    @GetMapping
    public List<Empresa> findAll() {
        return empresaService.findAll();
    }

    @ApiOperation(value = "Serviço responsável por obter uma empresa referente ao Id passado na URL.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a empresa."),
            @ApiResponse(code = 401, message = "Acesso não autorizado."),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
            @ApiResponse(code = 404, message = "Não foi encontrado um registro para o Id informado."),
            @ApiResponse(code = 500, message = "Foi gerado um erro no servidor."),
    })
    @GetMapping("/{id}")
    public Empresa findById(@PathVariable Long id) {
        return empresaService.findById(id);
    }

    @ApiOperation(value = "Serviço responsável por atualizar uma empresa referente ao Id passado na URL.")
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> update(@PathVariable("id") Long id, @RequestBody EmpresaRequest request) {
        CategoriaEmpresa categoria = categoriaEmpresaService.findById(request.getIdCategoria());

        Empresa empresaAtualizada = Empresa.fromRequest(request, categoria);
        empresaService.update(id, empresaAtualizada);
        
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Serviço responsável por alterar o status de uma empresa referente ao Id passado na URL.")
    @PutMapping("/{id}/updatestatus")
    public ResponseEntity<Empresa> updateStatus(@PathVariable("id") Long id, @RequestBody String novoStatus) {
        empresaService.updateStatus(id, novoStatus);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Serviço responsável por deletar uma empresa referente ao Id passado na URL.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        empresaService.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/findByUser/{id}")
    public Empresa findByUser(@PathVariable Long id) {
        Empresa empresa = empresaService.findByUsuario(id);

        return empresa;
    }


}
