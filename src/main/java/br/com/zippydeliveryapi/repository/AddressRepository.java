package br.com.zippydeliveryapi.repository;

import br.com.zippydeliveryapi.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
