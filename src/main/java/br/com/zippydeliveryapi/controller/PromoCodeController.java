package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.PromoCode;
import br.com.zippydeliveryapi.model.dto.request.PromoCodeRequest;
import br.com.zippydeliveryapi.service.PromoCodeService;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/promoCode")
@CrossOrigin
public class PromoCodeController {

    @Autowired
    private final PromoCodeService cupomDescontoService;

    public PromoCodeController(PromoCodeService cupomDescontoService) {
        this.cupomDescontoService = cupomDescontoService;
    }


    @PostMapping
    public ResponseEntity<PromoCode> save(@RequestBody @Valid PromoCodeRequest request) {
        return new ResponseEntity<>(this.cupomDescontoService.save(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<PromoCode> findAll() {
        return this.cupomDescontoService.findAll();
    }

    @GetMapping("/{id}")
    public PromoCode findById(@PathVariable Long id) {
        return this.cupomDescontoService.findById(id);
    }

    @GetMapping("/code/{code}")
    public PromoCode findByCode(@PathVariable String code) {
        return this.cupomDescontoService.findByCode(code);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromoCode> update(@PathVariable Long id, @RequestBody PromoCodeRequest request) {
        this.cupomDescontoService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.cupomDescontoService.delete(id);
        return ResponseEntity.ok().build();
    }
}