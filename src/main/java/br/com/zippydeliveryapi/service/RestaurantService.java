package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.Address;
import br.com.zippydeliveryapi.model.RestaurantCategory;
import br.com.zippydeliveryapi.model.Restaurant;
import br.com.zippydeliveryapi.model.User;
import br.com.zippydeliveryapi.model.dto.request.RestaurantRequest;
import br.com.zippydeliveryapi.model.dto.request.RestaurantStatusRequest;
import br.com.zippydeliveryapi.repository.RestaurantCategoryRepository;
import br.com.zippydeliveryapi.repository.RestaurantRepository;
import br.com.zippydeliveryapi.repository.AddressRepository;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import br.com.zippydeliveryapi.util.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RestaurantService {

    @Autowired
    private final RestaurantRepository restaurantRepository;

    @Autowired
    private final AddressRepository addressRepository;

    @Autowired
    private final RestaurantCategoryRepository restaurantCategoryRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             AddressRepository addressRepository,
                             RestaurantCategoryRepository restaurantCategoryRepository,
                             UserService userService,
                             PasswordEncoder passwordEncoder) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.restaurantCategoryRepository = restaurantCategoryRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Restaurant save(RestaurantRequest request) {
        User user = this.saveUser(request);
        Restaurant restaurant = Restaurant.fromRequest(request);
        restaurant.setEnabled(Boolean.TRUE);
        restaurant.setPassword(this.passwordEncoder.encode(request.getPassword()));
        restaurant.setUser(user);
        return this.restaurantRepository.save(restaurant);
    }

    @Transactional
    private Address saveAddress(RestaurantRequest request) {
        Address address = Address.builder()
                .street(StringUtils.hasText(request.getAddress().getStreet()) ? request.getAddress().getStreet() : "")
                .number(StringUtils.hasText(request.getAddress().getNumber()) ? request.getAddress().getNumber() : "")
                .district(StringUtils.hasText(request.getAddress().getDistrict()) ? request.getAddress().getDistrict() : "")
                .city(StringUtils.hasText(request.getAddress().getCity()) ? request.getAddress().getCity() : "")
                .state(StringUtils.hasText(request.getAddress().getState()) ? request.getAddress().getState() : "")
                .zipCode(StringUtils.hasText(request.getAddress().getZipCode()) ? request.getAddress().getZipCode() : "")
                .complement(StringUtils.hasText(request.getAddress().getComplement()) ? request.getAddress().getComplement() : "")
                .build();
        return this.addressRepository.save(address);
    }

    @Transactional
    private User saveUser(RestaurantRequest request) {
        try {
            User user = this.userService.findByUsername(request.getEmail());
            if (user == null) {
                user = User.builder()
                        .roles(Arrays.asList(User.ROLE_RESTAURANT))
                        .username(request.getEmail())
                        .password(request.getPassword())
                        .build();
                return this.userService.save(user);
            } else {
                throw new IllegalArgumentException("A user with this email already exists: " + request.getEmail());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void update(Long id, RestaurantRequest request) {
        Restaurant restaurant = this.findById(id);
        RestaurantCategory category = this.restaurantCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("RestaurantCategory", request.getCategoryId()));

        if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(restaurant.getEmail())) {
            restaurant.setEmail(request.getEmail());
            restaurant.getUser().setUsername(request.getEmail());
        }
        if (StringUtils.hasText(request.getPassword()) && !request.getPassword().equals(restaurant.getPassword())) {
            restaurant.setPassword(request.getPassword());
            restaurant.getUser().setPassword(request.getPassword());
        }
        if (StringUtils.hasText(request.getDoc()) && !request.getDoc().equals(restaurant.getDoc())) {
            restaurant.setDoc(request.getDoc());
        }

        restaurant.setCategory(category);
        restaurant.setAddress(this.saveAddress(request));
        restaurant.setName(StringUtils.hasText(request.getName()) ? request.getName() : restaurant.getName());
        restaurant.setDeliveryTime((request.getDeliveryTime() > 0) ? request.getDeliveryTime() : restaurant.getDeliveryTime());
        restaurant.setDeliveryFee((!Objects.equals(request.getDeliveryFee(), restaurant.getDeliveryFee())) ? request.getDeliveryFee() : restaurant.getDeliveryFee());
        restaurant.setPhone(StringUtils.hasText(request.getPhone()) ? request.getPhone() : restaurant.getPhone());
        restaurant.setProfileImage(StringUtils.hasText(request.getProfileImage()) ? request.getProfileImage() : restaurant.getProfileImage());
        restaurant.setCoverImage(StringUtils.hasText(request.getCoverImage()) ? request.getCoverImage() : restaurant.getCoverImage());
        restaurant.setAcceptedPaymentMethods(request.getAcceptedPaymentMethods());

        this.restaurantRepository.save(restaurant);
    }

    @Transactional
    public void updateStatus(Long id, RestaurantStatusRequest request) {
        Restaurant restaurant = this.findById(id);
        if (request.getStatus() == StatusEnum.ACTIVE || request.getStatus() == StatusEnum.INACTIVE || request.getStatus() == StatusEnum.PENDING) {
            if (request.getStatus() != StatusEnum.PENDING) {
                restaurant.setStatus(request.getStatus() == StatusEnum.ACTIVE ? 1 : 0);
            }
            this.restaurantRepository.save(restaurant);
        }
    }

    public List<Restaurant> findAll() {
        return this.restaurantRepository.findAll();
    }

    public Restaurant findById(Long id) {
        return this.restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant", id));
    }

    @Transactional
    public void delete(Long id) {
        Restaurant restaurant = this.restaurantRepository.findByIdAndEnabledTrue(id);
        if (restaurant != null) {
            restaurant.setEnabled(Boolean.FALSE);
            restaurant.setDoc(String.format("deleted-%d-%s", restaurant.getId(), restaurant.getDoc()));
            restaurant.setEmail(String.format("deleted-%d-%s", restaurant.getId(), restaurant.getEmail()));
            restaurant.setPhone(String.format("deleted-%d", restaurant.getId()));
            restaurant.setStatus(0);
            restaurant.getUser().setUsername(String.format("deleted-%d-%s", restaurant.getUser().getId(), restaurant.getUser().getUsername()));
            restaurant.getUser().setPassword(String.format("deleted-%d-%s", restaurant.getUser().getId(), restaurant.getUser().getPassword()));
            restaurant.getUser().setEnabled(Boolean.FALSE);
            this.restaurantRepository.save(restaurant);
        }
    }

    public Restaurant findByUserId(Long userId) {
        User user = this.userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant", userId));
        return this.restaurantRepository.findByUser(user);
    }
}