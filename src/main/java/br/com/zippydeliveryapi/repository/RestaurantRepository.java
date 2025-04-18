package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Restaurant;
import br.com.zippydeliveryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Restaurant findByUser(User user);

    Restaurant findByIdAndEnabledTrue(Long id);
}
