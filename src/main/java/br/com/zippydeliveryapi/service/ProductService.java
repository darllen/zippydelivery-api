package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.ProductCategory;
import br.com.zippydeliveryapi.model.Restaurant;
import br.com.zippydeliveryapi.model.Product;
import br.com.zippydeliveryapi.model.dto.request.ProductRequest;
import br.com.zippydeliveryapi.model.dto.response.ProductResponse;
import br.com.zippydeliveryapi.repository.ProductCategoryRepository;
import br.com.zippydeliveryapi.repository.RestaurantRepository;
import br.com.zippydeliveryapi.repository.ProductRepository;
import br.com.zippydeliveryapi.util.exception.EntityNotFoundException;
import br.com.zippydeliveryapi.util.exception.ProductException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired 
    private final RestaurantRepository restaurantRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, RestaurantRepository restaurantRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public ProductResponse save(ProductRequest request) {
        if (!request.getAvailable()) {
            throw new ProductException(ProductException.MESSAGE_PRODUCT_UNAVAILABLE);
        }

        ProductCategory productCategory = this.productCategoryRepository.findById(request.getProductCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("ProductCategory", request.getProductCategoryId()));
        if(!productCategory.getEnabled()){
            throw new ProductException("Category disabled");
        }

        Restaurant restaurant = this.restaurantRepository.findByIdAndEnabledTrue(request.getRestaurantId());
        if (restaurant == null) {
            throw new EntityNotFoundException("Restaurant", request.getRestaurantId());
        }

        Product product = Product.fromRequest(request);
        product.setRestaurant(restaurant);
        product.setProductCategory(productCategory);
        product.setEnabled(Boolean.TRUE);

        Product productSaved = productRepository.save(product);

        return productRepository.findProjectedById(productSaved.getId());
    }

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    public Product findById(Long id) {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
    }

    public List<ProductResponse> findByCategoryId(Long categoryId) {
        return this.productRepository.findByCategoryIdAndEnabledTrue(categoryId);
    }

    public List<ProductResponse> findByRestaurantId(Long categoryId) {
        return this.productRepository.findByRestaurantIdAndEnabledTrue(categoryId);
    }

    @Transactional
    public void update(Long id, ProductRequest request) {
        Product product = this.findById(id);
        ProductCategory categoryProduct = this.productCategoryRepository.findById(request.getProductCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("ProductCategory", request.getProductCategoryId()));
        if(!categoryProduct.getEnabled()){
            throw new ProductException("Category disabled");
        }
        product.setProductCategory(categoryProduct);
        product.setDescription(request.getDescription());
        product.setTitle(request.getTitle());
        product.setImageUrl(request.getImageUrl());
        product.setPrice(request.getPrice());
        product.setAvailable(request.getAvailable());
        this.productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = this.findById(id);
        product.setEnabled(Boolean.FALSE);
        this.productRepository.save(product);
    }

    public List<List<Object>> groupByCategory() {
        List<Object[]> productsByCategory = this.productRepository.groupByCategory();
        Map<Long, List<Object>> categoriesMap = new HashMap<>();

        for (Object[] result : productsByCategory) {
            ProductCategory productCategory = (ProductCategory) result[0];
            Product product = (Product) result[1];

            if (product != null && product.getEnabled()) {
                categoriesMap.computeIfAbsent(productCategory.getId(), k -> new ArrayList<>()).add(product);
            }
        }
        return new ArrayList<>(categoriesMap.values());
    }

    public List<List<Object>> groupByRestaurantCategory(Long id) {
        List<Object[]> productsByCategory = this.productRepository.findByRestaurantGroupByCategory(id);
        Map<Long, List<Object>> categoriesMap = new HashMap<>();

        for (Object[] result : productsByCategory) {
            ProductCategory productCategory = (ProductCategory) result[0];
            Object product = result[1];

            if (product != null) {
                categoriesMap.computeIfAbsent(productCategory.getId(), k -> new ArrayList<>()).add(product);
            }
        }
        return new ArrayList<>(categoriesMap.values());
    }
}