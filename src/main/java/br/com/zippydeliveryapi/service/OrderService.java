package br.com.zippydeliveryapi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import br.com.zippydeliveryapi.model.*;
import br.com.zippydeliveryapi.model.dto.request.OrderItemRequest;
import br.com.zippydeliveryapi.model.dto.request.OrderRequest;
import br.com.zippydeliveryapi.model.dto.request.UpdateOrderRequest;
import br.com.zippydeliveryapi.repository.*;
import br.com.zippydeliveryapi.util.exception.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.zippydeliveryapi.model.dto.response.DashBoardResponse;
import br.com.zippydeliveryapi.util.enums.StatusEnum;
import br.com.zippydeliveryapi.util.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.util.StringUtils;

@Service
public class OrderService {

    @Autowired
    private final OrderRepository repository;

    @Autowired
    private final PromoCodeService promoCodeService;

    @Autowired
    private final RestaurantRepository restaurantRepository;

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final AddressRepository addressRepository;

    @Autowired
    private final ProductRepository productRepository;

    public OrderService(OrderRepository repository, PromoCodeService promoCodeService, RestaurantRepository restaurantRepository, CustomerRepository customerRepository, AddressRepository addressRepository, ProductRepository productRepository) {
        this.repository = repository;
        this.promoCodeService = promoCodeService;
        this.restaurantRepository = restaurantRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order save(OrderRequest request) {
        if(request.getOrderItems().isEmpty()) throw new ProductException(ProductException.MESSAGE_PRODUCT_UNAVAILABLE);

        Customer customer = this.customerRepository.findByIdAndEnabledTrue(request.getCustomerId());
        if (customer == null) throw new EntityNotFoundException("Customer", request.getCustomerId());

        Restaurant restaurant = this.restaurantRepository.findByIdAndEnabledTrue(request.getRestaurantId());
        if (restaurant == null) throw new EntityNotFoundException("Restaurant", request.getRestaurantId());

        PromoCode promoCode = null;
        if (StringUtils.hasText(request.getPromoCode())) {
            promoCode = this.promoCodeService.findByCode(request.getPromoCode());
        }

        Address address = this.addressRepository.findById(request.getDeliveryAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Delivery address", request.getDeliveryAddressId()));

        List<OrderItem> orderItems = this.createOrderList(request.getOrderItems());
        Double valorTotalOrder = this.calculateTotalOrderValue(orderItems, promoCode);

        Order order = Order.fromRequest(request);
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setPromoCode(promoCode);
        order.setDeliveryAddress(address);
        order.setDeliveryFee(restaurant.getDeliveryFee());
        order.setOrderItems(orderItems);
        order.setTotalAmount(valorTotalOrder);
        order.setEnabled(Boolean.TRUE);
        order.setDateTime(LocalDateTime.now());

        return this.repository.save(order);
    }

    private List<OrderItem> createOrderList(List<OrderItemRequest> itens) {
        return itens.stream()
                .map(this::convertToOrderItem)
                .toList();
    }

    private OrderItem convertToOrderItem(OrderItemRequest request) {
        Product product = this.productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product", request.getProductId()));
        return OrderItem.builder()
                .product(product)
                .quantity(request.getProductQuantity())
                .unitPrice(request.getUnitPrice())
                .totalPrice(request.getUnitPrice() * request.getProductQuantity())
                .build();
    }

    private Double calculateTotalOrderValue(List<OrderItem> orderItems, PromoCode promoCode) {
        Double totalItems = orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        if(promoCode != null && this.promoCodeService.validateCoupon(promoCode)){
            return this.promoCodeService.applyCoupon(totalItems, promoCode);
        }
        return totalItems;
    }

    public List<Order> findAll() {
        return this.repository.findAll();
    }

    public Order findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order", id));
    }

    public List<Order> findByRestaurantId(Long restaurantId) {
        return this.repository.findByRestaurantId(restaurantId);
    }

    public List<Order> findByCustomerId(Long customerId) {
        return this.repository.findByCustomerId(customerId);
    }

    @Transactional
    public void update(Long id, UpdateOrderRequest request) {
        Order order = this.findById(id);
        Address deliveryAddress = this.addressRepository.findById(request.getDeliveryAddressId())
                .orElseThrow(() -> new EntityNotFoundException("Address", id));

        order.setPaymentMethod(request.getPaymentMethod().getCode());
        order.setOrderStatus(request.getOrderStatus().getCode());
        order.setPaymentStatus(request.getPaymentStatus().getCode());
        order.setDeliveryAddress(deliveryAddress);
        this.repository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        Order order = this.findById(id);
        order.setEnabled(Boolean.FALSE);
        order.setOrderStatus(StatusEnum.CANCELED.getCode());
        order.setPaymentStatus(StatusEnum.REFUNDED.getCode());
        this.repository.save(order);
    }

    public DashBoardResponse getDashboard(Long id) {
        List<Order> orders = this.findByRestaurantId(id);
        DashBoardResponse response = new DashBoardResponse();
        double vendasHojeValor = 0.0;

        response.setTotalSales(orders.size());
        response.setTotalRevenue(0.0);
        response.setSalesToday(0);

        for (Order order : orders) {
            response.setTotalRevenue(response.getTotalRevenue() + order.getTotalAmount());
            if (order.getDateTime().toLocalDate().equals(LocalDate.now())) {
                response.setSalesToday(response.getSalesToday() + 1);
                vendasHojeValor = vendasHojeValor + order.getTotalAmount();
            }
        }
        response.setAverageRevenue(vendasHojeValor / response.getSalesToday());

        return response;
    }

    public List<DashBoardResponse> getMonthlyDashboard(Long id) {
        List<Order> orders = this.findByRestaurantId(id);
        List<DashBoardResponse> responses = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            responses.add(new DashBoardResponse());
        }
        for (Order order : orders) {
            int mes = order.getDateTime().getMonthValue() - 1;
            DashBoardResponse response = responses.get(mes);

            if (response.getTotalSales() == null) {
                response.setTotalSales(0);
            }
            response.setTotalSales(response.getTotalSales() + 1);
            if (response.getTotalRevenue() == null) {
                response.setTotalRevenue(0.0);
            }
            response.setTotalRevenue(response.getTotalRevenue() + order.getTotalAmount());
        }
        for (DashBoardResponse dashBoardResponse : responses) {
            if (dashBoardResponse.getTotalSales() == null) {
                dashBoardResponse.setTotalSales(0);
            }
            if (dashBoardResponse.getTotalRevenue() == null) {
                dashBoardResponse.setTotalRevenue(0.0);
            }
            if (dashBoardResponse.getTotalSales() > 0) {
                dashBoardResponse.setAverageRevenue(
                        dashBoardResponse.getTotalRevenue() / dashBoardResponse.getTotalSales());
            }
        }
        return responses;
    }

    public DashBoardResponse getAllDashboards() {
        List<Order> orders = this.findAll();
        DashBoardResponse response = new DashBoardResponse();
        double vendasHojeValor = 0.0;

        response.setTotalSales(orders.size());
        response.setTotalRevenue(0.0);
        response.setSalesToday(0);

        for (Order order : orders) {
            response.setTotalRevenue(response.getTotalRevenue() + order.getTotalAmount());
            if (order.getDateTime().toLocalDate().equals(LocalDate.now())) {
                response.setSalesToday(response.getSalesToday() + 1);
                vendasHojeValor = vendasHojeValor + order.getTotalAmount();
            }
        }
        response.setAverageRevenue(vendasHojeValor / response.getSalesToday());

        return response;
    }

    public List<DashBoardResponse> getAllMonthlyDashboards() {
        List<Order> orders = this.findAll();
        List<DashBoardResponse> responses = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            responses.add(new DashBoardResponse());
        }
        for (Order order : orders) {
            int mes = order.getDateTime().getMonthValue() - 1;
            DashBoardResponse response = responses.get(mes);

            if (response.getTotalSales() == null) {
                response.setTotalSales(0);
            }
            response.setTotalSales(response.getTotalSales() + 1);

            if (response.getTotalRevenue() == null) {
                response.setTotalRevenue(0.0);
            }
            response.setTotalRevenue(response.getTotalRevenue() + order.getTotalAmount());
        }
        for (DashBoardResponse dashBoardResponse : responses) {
            if (dashBoardResponse.getTotalSales() == null) {
                dashBoardResponse.setTotalSales(0);
            }
            if (dashBoardResponse.getTotalRevenue() == null) {
                dashBoardResponse.setTotalRevenue(0.0);
            }
            if (dashBoardResponse.getTotalSales() > 0) {
                dashBoardResponse.setAverageRevenue(
                        dashBoardResponse.getTotalRevenue() / dashBoardResponse.getTotalSales());
            }
        }
        return responses;
    }
}
