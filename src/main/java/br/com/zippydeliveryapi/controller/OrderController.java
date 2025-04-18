package br.com.zippydeliveryapi.controller;

import java.util.List;
import br.com.zippydeliveryapi.model.Order;
import br.com.zippydeliveryapi.model.dto.request.OrderRequest;
import br.com.zippydeliveryapi.model.dto.request.UpdateOrderRequest;
import br.com.zippydeliveryapi.model.dto.response.DashBoardResponse;
import br.com.zippydeliveryapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> save(@RequestBody @Valid OrderRequest request) {
        return new ResponseEntity<>(this.orderService.save(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Order> findAll() {
        return this.orderService.findAll();
    }

    @GetMapping("/{id}")
    public Order findById(@PathVariable Long id) {
        return this.orderService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable("id") Long id, @RequestBody UpdateOrderRequest request) {
        this.orderService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.orderService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboard/{id}")
    public DashBoardResponse getDashboard(@PathVariable Long id) {
        return this.orderService.getDashboard(id);
    }

    @GetMapping("/dashboard-monthly/{id}")
    public List<DashBoardResponse> getMonthlyDashboard(@PathVariable Long id) {
        return this.orderService.getMonthlyDashboard(id);
    }

    @GetMapping("/dashboard-all")
    public DashBoardResponse getAllDashboards() {
        return this.orderService.getAllDashboards();
    }

    @GetMapping("/dashboard-monthly-all")
    public List<DashBoardResponse> getAllMonthlyDashboards() {
        return this.orderService.getAllMonthlyDashboards();
    }
    
    @GetMapping("/customer/{id}")
    public List<Order> findOrdersByCustomer(@PathVariable Long id) {
        return this.orderService.findByCustomerId(id);
    }

    @GetMapping("/restaurant/{id}")
    public List<Order> findOrdersByRestaurant(@PathVariable Long id) {
        return this.orderService.findByRestaurantId(id);
    }
}