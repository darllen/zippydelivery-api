package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Product;
import br.com.zippydeliveryapi.model.dto.response.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT c AS category, p FROM ProductCategory c LEFT JOIN Product p ON c.id = p.category.id")
    List<Object[]> groupByCategory();

    @Query("SELECT c AS category, p FROM ProductCategory c LEFT JOIN Product p ON c.id = p.category.id WHERE c.restaurant.id = :restaurantId")
    List<Object[]> findByRestaurantGroupByCategory(@Param("restaurantId") Long restaurantId);

    List<ProductResponse> findByCategoryIdAndEnabledTrue(Long categoryId);

    List<ProductResponse> findByRestaurantIdAndEnabledTrue(Long restaurantId);

    ProductResponse findProjectedById(Long id);

}