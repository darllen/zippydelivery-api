package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT p FROM Order p WHERE p.restaurant.id = :restaurantId")
    List<Order> findByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query(value = "SELECT p FROM Order p WHERE p.customer.id = :customerId")
    List<Order> findByCustomerId(@Param("customerId") Long customerId);

}
