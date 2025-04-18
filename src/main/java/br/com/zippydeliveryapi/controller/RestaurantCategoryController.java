package br.com.zippydeliveryapi.controller;

import br.com.zippydeliveryapi.model.RestaurantCategory;
import br.com.zippydeliveryapi.model.dto.request.RestaurantCategoryRequest;
import br.com.zippydeliveryapi.service.RestaurantCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/restaurant-category")
public class RestaurantCategoryController {

    @Autowired
    private final RestaurantCategoryService restaurantCategoryService;

    public RestaurantCategoryController(RestaurantCategoryService restaurantCategoryService) {
        this.restaurantCategoryService = restaurantCategoryService;
    }

    @PostMapping
    public ResponseEntity<RestaurantCategory> save(@RequestBody @Valid RestaurantCategoryRequest request) {
        RestaurantCategory restaurantCategory = request.build();
        RestaurantCategory restaurantCategoryNova = this.restaurantCategoryService.save(restaurantCategory);
        return new ResponseEntity<>(restaurantCategoryNova, HttpStatus.CREATED);
    }

    @GetMapping
    public List<RestaurantCategory> findAll() {
        return this.restaurantCategoryService.findAll();
    }

    @GetMapping("/{id}")
    public RestaurantCategory findById(@PathVariable Long id) {
        return this.restaurantCategoryService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantCategory> update(@PathVariable("id") Long id, @RequestBody RestaurantCategoryRequest request) {
        this.restaurantCategoryService.update(id, request.build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.restaurantCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }
}
