package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.Courier;
import br.com.zippydeliveryapi.model.dto.request.CourierRequest;
import br.com.zippydeliveryapi.model.dto.request.CourierStatusRequest;
import br.com.zippydeliveryapi.service.CourierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courier")
@CrossOrigin
public class CourierController {
    
    @Autowired
    private final CourierService courierService;

    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @GetMapping
    public List<Courier> findAll() {
        return this.courierService.findAll();
    }

    @PostMapping
    public ResponseEntity<Courier> save(@RequestBody @Valid CourierRequest request) { // se tiver tempo melhoro isso, o ideal é retornar um dto, sem info sensíveis
        return new ResponseEntity<>(this.courierService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Courier> update(@PathVariable("id") Long id, @RequestBody CourierRequest request) {
        this.courierService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.courierService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{id}")
    public Courier findByUser(@PathVariable Long id) {
        return this.courierService.findByUserId(id);
    }

    @PutMapping("/{id}/updatestatus")
    public ResponseEntity<Courier> updateStatus(@PathVariable("id") Long id, @RequestBody CourierStatusRequest request) {
        this.courierService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok().build();
    }
}
