package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findByRestaurantIdAndEnabledTrue(Long empresaId);

}
