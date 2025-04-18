package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Customer;
import br.com.zippydeliveryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByUser(Optional<User> user);

    Customer findByIdAndEnabledTrue(Long id);

}