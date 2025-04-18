package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}

