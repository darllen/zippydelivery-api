package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {

    List<RestaurantCategory> findByEnabledTrue();

}
