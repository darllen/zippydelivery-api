package br.com.zippydeliveryapi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import br.com.zippydeliveryapi.model.Address;
import br.com.zippydeliveryapi.model.dto.request.AddressRequest;
import br.com.zippydeliveryapi.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import br.com.zippydeliveryapi.model.Customer;
import br.com.zippydeliveryapi.model.User;
import br.com.zippydeliveryapi.model.dto.request.CustomerRequest;
import br.com.zippydeliveryapi.repository.CustomerRepository;
import br.com.zippydeliveryapi.util.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final AddressRepository addressRepository;

    public CustomerService(CustomerRepository customerRepository, UserService userService, PasswordEncoder passwordEncoder, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.addressRepository = addressRepository;
    }

    // TODO endpoint para atualizar endereços

    @Transactional
    public Customer save(CustomerRequest request) {
        User user = this.saveUser(request);
        Customer cliente = Customer.fromRequest(request);
        cliente.setPassword(this.passwordEncoder.encode(request.getPassword()));
        cliente.setUser(user);
        cliente.setAddress(new ArrayList<>());
        cliente.setEnabled(Boolean.TRUE);
        return this.customerRepository.save(cliente);
    }

    @Transactional
    private User saveUser(CustomerRequest request) {
        User user = null;
        try {
            user = this.userService.findByUsername(request.getEmail());
            if(user == null){
                user = User.builder()
                        .roles(Arrays.asList(User.ROLE_RESTAURANT))
                        .username(request.getEmail())
                        .password(request.getPassword())
                        .build();
                return this.userService.save(user);
            } else {
                throw new IllegalArgumentException("Já existe um usuário com o e-mail: " + request.getEmail());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o usuário: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void update(Long id, CustomerRequest request) {
        Customer cliente = this.findById(id);
        
        if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(cliente.getEmail())) {
            cliente.setEmail(request.getEmail());
            cliente.getUser().setUsername(request.getEmail());
        }
        
        cliente.setName(request.getName());
        
        if (StringUtils.hasText(request.getPassword())) {
            String senhaCodificada = this.passwordEncoder.encode(request.getPassword());
            cliente.setPassword(senhaCodificada);
            cliente.getUser().setPassword(senhaCodificada);
        }
    
        this.customerRepository.save(cliente);
    }

    public List<Customer> findAll() {
        return this.customerRepository.findAll();
    }

    public Customer findByUserId(Long userId) {
        Optional<User> user = this.userService.findById(userId);
        return this.customerRepository.findByUser(user);
    }

    public Customer findById(Long id) {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer", id));
    }

    public Address findAddressById(Long addressId) {
        return this.addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço", addressId));
    }

    @Transactional
    public void delete(Long id) {
        Customer cliente = this.customerRepository.findByIdAndEnabledTrue(id);
        if(cliente != null){
            cliente.setEnabled(Boolean.FALSE);
            cliente.setDoc(String.format("deleted-%d-%s", cliente.getId(), cliente.getDoc()));
            cliente.setEmail(String.format("deleted-%d-%s", cliente.getId(), cliente.getEmail()));
            cliente.getUser().setUsername(String.format("deleted-%d-%s", cliente.getUser().getId(), cliente.getUser().getUsername()));
            cliente.getUser().setPassword(String.format("deleted-%d-%s", cliente.getUser().getId(), cliente.getUser().getPassword()));
            cliente.getUser().setEnabled(Boolean.FALSE);
            this.customerRepository.save(cliente);
        }
    }

    @Transactional
    public Address saveNewAddress(Long id, AddressRequest request) {
        Address address = Address.fromRequest(request);
        Customer cliente = this.findById(id);

        List<Address> listaAddress = cliente.getAddress();
        address.setEnabled(Boolean.TRUE);
        address.setDefaultForDelivery(listaAddress.isEmpty());

        listaAddress.add(address);

        cliente.setAddress(listaAddress);
        this.update(cliente.getId(), CustomerRequest.fromEntity(cliente));
        return this.addressRepository.save(address);
    }

    public List<Address> findAllAddress(Long id) {
        Customer cliente = this.findById(id);
        return cliente.getAddress();
    }

    @Transactional
    public void deleteAddress(Long id, Long addressId) {
        Customer cliente = this.findById(id);
        Address address = this.findAddressById(addressId);

        address.setEnabled(Boolean.FALSE);
        this.addressRepository.save(address);
        for (Address e : cliente.getAddress()) {
            if (e.getId().equals(addressId)) {
                e.setEnabled(Boolean.FALSE);
                break;
            }
        }
        this.customerRepository.save(cliente);
    }

    @Transactional
    public void updateAddress(Long id, Long addressId, AddressRequest request) {
        Customer cliente = this.findById(id);
        Address address = this.findAddressById(addressId);
        address.setStreet(request.getStreet() == null ? address.getStreet() : request.getStreet());
        address.setNumber(request.getNumber() == null ? address.getNumber() : request.getNumber());
        address.setDistrict(request.getDistrict() == null ? address.getDistrict() : request.getDistrict());
        address.setCity(request.getCity() == null ? address.getCity() : request.getCity());
        address.setState(request.getState() == null ? address.getState() : request.getState());
        address.setZipCode(request.getZipCode() == null ? address.getZipCode() : request.getZipCode());
        address.setComplement(request.getComplement() == null ? address.getComplement() : request.getComplement());
        this.addressRepository.save(address);

        for (Address e : cliente.getAddress()) {
            if (e.getId().equals(addressId)) {
                e.setStreet(request.getStreet() == null ? e.getStreet() : request.getStreet());
                e.setNumber(request.getNumber() == null ? e.getNumber() : request.getNumber());
                e.setDistrict(request.getDistrict() == null ? e.getDistrict() : request.getDistrict());
                e.setCity(request.getCity() == null ? e.getCity() : request.getCity());
                e.setState(request.getState() == null ? e.getState() : request.getState());
                e.setZipCode(request.getZipCode() == null ? e.getZipCode() : request.getZipCode());
                e.setComplement(request.getComplement() == null ? e.getComplement() : request.getComplement());
                break;
            }
        }
        this.customerRepository.save(cliente);
    }

    @Transactional
    public void chooseDefaultAddress(Long id, Long addressId) {
        Customer cliente = this.findById(id);
        Address addressParaMarcar = this.findAddressById(addressId);

        List<Address> listaAddress = cliente.getAddress();
        cliente.getAddress().forEach(address -> {
            if (address.getId().equals(addressParaMarcar.getId())) {
                address.setDefaultForDelivery(Boolean.TRUE);
            } else {
                address.setDefaultForDelivery(Boolean.FALSE);
            }
            this.addressRepository.save(address);
        });
        cliente.setAddress(listaAddress);
        this.customerRepository.save(cliente);
    }

    public Address findDefaultAddress(Long id) {
        Customer cliente = this.findById(id);
        List<Address> listaAddress = cliente.getAddress();
        for (Address address : listaAddress) {
            Address e = this.addressRepository.findById(address.getId()).get();
            if (e.isDefaultForDelivery()) {
                return e;
            }
        }
        return null;
    }
}
