package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Courier;
import br.com.zippydeliveryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CourierRepository extends JpaRepository<Courier, Long> {

    Courier findByUser(Optional<User> user);

    Courier findByIdAndEnabledTrue(Long id);
}