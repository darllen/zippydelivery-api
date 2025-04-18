package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.ProductCategory;
import br.com.zippydeliveryapi.util.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import br.com.zippydeliveryapi.repository.ProductCategoryRepository;

@Service
public class ProductCategoryService {

    @Autowired
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Transactional
    public ProductCategory save(ProductCategory ProductCategory) {
        ProductCategory.setEnabled(Boolean.TRUE);
        return this.productCategoryRepository.save(ProductCategory);
    }

    public List<ProductCategory> findAll() {
        return this.productCategoryRepository.findAll();
    }

    public ProductCategory findById(Long id) {
        return this.productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductCategory", id));
    }

    public List<ProductCategory> findByRestaurantId(Long id) {
        return this.productCategoryRepository.findByRestaurantIdAndEnabledTrue(id);
    }

    @Transactional
    public void update(Long id, ProductCategory novaCategoria) {
        ProductCategory ProductCategory = this.findById(id);
        ProductCategory.setDescription(novaCategoria.getDescription());
        this.productCategoryRepository.save(ProductCategory);
    }

    @Transactional
    public void delete(Long id) {
        ProductCategory ProductCategory = this.findById(id);
        ProductCategory.setEnabled(Boolean.FALSE);
        this.productCategoryRepository.save(ProductCategory);
    }
}