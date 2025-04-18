package br.com.zippydeliveryapi.controller;

import br.com.zippydeliveryapi.model.Restaurant;
import br.com.zippydeliveryapi.model.dto.request.RestaurantRequest;
import br.com.zippydeliveryapi.model.dto.request.RestaurantStatusRequest;
import br.com.zippydeliveryapi.service.RestaurantService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }


    @PostMapping
    public ResponseEntity<Restaurant> save(@RequestBody @Valid RestaurantRequest request) {
        return new ResponseEntity<>(this.restaurantService.save(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Restaurant> findAll() {
        return this.restaurantService.findAll();
    }

    @GetMapping("/{id}")
    public Restaurant findById(@PathVariable Long id) {
        return this.restaurantService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> update(@PathVariable("id") Long id, @RequestBody RestaurantRequest request) {
        this.restaurantService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateStatus(@PathVariable("id") Long id, @RequestBody RestaurantStatusRequest request) {
        this.restaurantService.updateStatus(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.restaurantService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{id}")
    public Restaurant findByUser(@PathVariable Long id) {
        return this.restaurantService.findByUserId(id);
    }
}