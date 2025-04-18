package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.Product;
import br.com.zippydeliveryapi.model. ProductCategory;
import br.com.zippydeliveryapi.model.dto.request. ProductCategoryRequest;
import br.com.zippydeliveryapi.model.dto.response.ProductResponse;
import br.com.zippydeliveryapi.service.ProductCategoryService;
import br.com.zippydeliveryapi.service.RestaurantService;
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
@RequestMapping("/api/product-category")
@CrossOrigin
public class ProductCategoryController {

    @Autowired
    private final ProductCategoryService productCategoryService;

    @Autowired
    private final RestaurantService restaurantService;

    @Autowired
    private final ProductService productService;

    public ProductCategoryController(ProductCategoryService productCategoryService, RestaurantService restaurantService, ProductService productService) {
        this.productCategoryService = productCategoryService;
        this.restaurantService = restaurantService;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity< ProductCategory> save(@RequestBody @Valid ProductCategoryRequest request) {
        ProductCategory productCategory = request.build();
        productCategory.setRestaurant(this.restaurantService.findById(request.getRestaurantId()));
        ProductCategory newProductCategory = this.productCategoryService.save(productCategory);
        return new ResponseEntity<>(newProductCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public List< ProductCategory> findAll() {
        return this.productCategoryService.findAll();
    }

    @GetMapping("/{id}")
    public ProductCategory findById(@PathVariable Long id) {
        return this.productCategoryService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity< ProductCategory> update(@PathVariable("id") Long id, @RequestBody ProductCategoryRequest request) {
        this.productCategoryService.update(id, request.build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        List<ProductResponse> products = this.productService.findByCategoryId(id);
        for (ProductResponse p : products) {
            this.productService.delete(p.getId());
        }
        this.productCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/restaurant/{id}")
    public List<ProductCategory> findProductCategoriesByRestaurant(@PathVariable("id") Long restaurantId) {
        return this.productCategoryService.findByRestaurantId(restaurantId);
    }
}