package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.Product;
import br.com.zippydeliveryapi.model.dto.request.ProductRequest;
import br.com.zippydeliveryapi.model.dto.response.ProductResponse;
import br.com.zippydeliveryapi.service.ProductService;
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
@RequestMapping("/api/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping
    public ResponseEntity<ProductResponse> save(@RequestBody @Valid ProductRequest request) {
        return new ResponseEntity<>(this.productService.save(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Product> findAll() {
        return this.productService.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return this.productService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest request) {
        this.productService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category")
    public List<List<Object>> groupByCategory() {
        return this.productService.groupByCategory();
    }

     @GetMapping("/restaurant-category/{id}")
    public List<List<Object>> groupByRestaurantCategory(@PathVariable("id") Long restaurantId) {
        return this.productService.groupByRestaurantCategory(restaurantId);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<ProductResponse> findByRestaurant(@PathVariable Long restaurantId) {
        return this.productService.findByRestaurantId(restaurantId);
    }
}