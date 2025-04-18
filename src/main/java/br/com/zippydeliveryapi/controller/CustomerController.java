package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.Customer;
import br.com.zippydeliveryapi.model.Address;
import br.com.zippydeliveryapi.model.dto.request.CustomerRequest;
import br.com.zippydeliveryapi.model.dto.request.AddressRequest;
import br.com.zippydeliveryapi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> save(@RequestBody @Valid CustomerRequest request) {
        return new ResponseEntity<>(this.customerService.save(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Customer> findAll() {
        return this.customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer findById(@PathVariable Long id) {
        return this.customerService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody CustomerRequest request) {
        this.customerService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.customerService.delete(id);
        return ResponseEntity.ok().build();
    }
  
    @GetMapping("/user/{id}")
    public Customer findByUser(@PathVariable Long id) {
        return this.customerService.findByUserId(id);
    }

    @PostMapping("{customerId}/address")
    public ResponseEntity<Address> saveNewAddress(@PathVariable("customerId") Long id, @RequestBody @Valid AddressRequest request) {
        return new ResponseEntity<>(this.customerService.saveNewAddress(id, request), HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}/address")
    public List<Address> findAllAddress(@PathVariable("customerId") Long id) {
        return this.customerService.findAllAddress(id);
    }

    @PutMapping("/{customerId}/address/{addressId}")
    public ResponseEntity<Customer> updateAddress(@PathVariable Long customerId, @PathVariable Long addressId, @RequestBody AddressRequest request) {
        this.customerService.updateAddress(customerId, addressId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{customerId}/address/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long customerId, @PathVariable Long addressId) {
        this.customerService.deleteAddress(customerId, addressId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{customerId}/address/default/{addressId}")
    public ResponseEntity<Void> chooseDefaultAddress(@PathVariable Long customerId, @PathVariable Long addressId) {
        this.customerService.chooseDefaultAddress(customerId, addressId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{customerId}/address/default")
    public Address findDefaultAddress(@PathVariable("customerId") Long id) {
        return this.customerService.findDefaultAddress(id);
    }
}