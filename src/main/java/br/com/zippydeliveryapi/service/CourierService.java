package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.Courier;
import br.com.zippydeliveryapi.model.User;
import br.com.zippydeliveryapi.model.dto.request.CourierRequest;
import br.com.zippydeliveryapi.repository.CourierRepository;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import br.com.zippydeliveryapi.util.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CourierService {

    @Autowired
    private final CourierRepository courierRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public CourierService(CourierRepository repository, UserService userService, PasswordEncoder passwordEncoder) {
        this.courierRepository = repository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Courier> findAll() {
        return this.courierRepository.findAll();
    }

    public Courier findByUserId(Long userId) {
        Optional<User> user = this.userService.findById(userId);
        return this.courierRepository.findByUser(user);
    }

    public Courier findById(Long id) {
        return this.courierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rider", id));
    }

    @Transactional
    public Courier save(CourierRequest request) {
        User user = this.saveUser(request);
        Courier courier = Courier.fromRequest(request);
        courier.setEnabled(Boolean.TRUE);
        courier.setPassword(this.passwordEncoder.encode(courier.getPassword()));
        courier.setUser(user);
        return this.courierRepository.save(courier);
    }

    @Transactional
    public void update(Long id, CourierRequest request) {
        Courier courier = this.findById(id);

        if(StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(courier.getEmail())){
            courier.setEmail(request.getEmail());
            courier.getUser().setUsername(request.getEmail());
        }
        if(StringUtils.hasText(request.getPassword()) && !request.getPassword().equals(courier.getPassword())){
            courier.setPassword(request.getPassword());
            courier.getUser().setPassword(request.getPassword());
        }
        courier.setName(request.getName());
        courier.setBirthDate(request.getBirthDate());
        courier.setVehicle(request.getVehicle());
        courier.setPlate(request.getPlate());
        courier.setPhone(request.getPhone());
        this.courierRepository.save(courier);
    }

    @Transactional
    public void updateStatus(Long id, StatusEnum novoStatus) {
        Courier courier = this.findById(id);
        courier.setStatus(novoStatus.getCode());
        this.courierRepository.save(courier);
    }

    @Transactional
    public void delete(Long id) {
        Courier courier = this.courierRepository.findByIdAndEnabledTrue(id);
        if(courier != null) {
            courier.setEnabled(Boolean.FALSE);
            courier.setDoc(String.format("deleted-%d-%s", courier.getId(), courier.getDoc()));
            courier.setEmail(String.format("deleted-%d-%s", courier.getId(), courier.getEmail()));
            courier.getUser().setUsername(String.format("deleted-%d-%s", courier.getUser().getId(), courier.getUser().getUsername()));
            courier.getUser().setPassword(String.format("deleted-%d-%s", courier.getUser().getId(), courier.getUser().getPassword()));
            courier.getUser().setEnabled(Boolean.FALSE);
            this.courierRepository.save(courier);
        }
    }

    @Transactional
    private User saveUser(CourierRequest request) {
        User user = null;
        try {
            user = this.userService.findByUsername(request.getEmail());
            if(user == null){
                user = User.builder()
                        .roles(Arrays.asList(User.ROLE_RIDER))
                        .username(request.getEmail())
                        .password(request.getPassword())
                        .build();
                return this.userService.save(user);
            } else {
                throw new IllegalArgumentException(String.format("A user with the email %s already exists.", request.getEmail()));
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error while saving the user: %s", e.getMessage()), e);
        }
    }
}