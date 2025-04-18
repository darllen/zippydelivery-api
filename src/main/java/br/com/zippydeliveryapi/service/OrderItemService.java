package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.OrderItem;
import br.com.zippydeliveryapi.repository.OrderItemRepository;
import br.com.zippydeliveryapi.util.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public OrderItem save(OrderItem orderItem) {
        orderItem.setEnabled(Boolean.TRUE);
        orderItem.setTotalPrice(orderItem.getUnitPrice() * orderItem.getQuantity());
        return this.orderItemRepository.save(orderItem);
    }

    public List<OrderItem> findAll() {
        return this.orderItemRepository.findAll();
    }

    public OrderItem findById(Long id) {
        return this.orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem", id));
    }

    @Transactional
    public void update(Long id, OrderItem orderItemChanged) {
        OrderItem orderItem = this.findById(id);
        orderItem.setProduct(orderItemChanged.getProduct());
        orderItem.setOrder(orderItemChanged.getOrder());
        orderItem.setQuantity(orderItemChanged.getQuantity());
        orderItem.setUnitPrice(orderItemChanged.getUnitPrice());
        orderItem.setTotalPrice(orderItemChanged.getTotalPrice() * orderItemChanged.getQuantity());
        this.orderItemRepository.save(orderItem);
    }

    @Transactional
    public void delete(Long id) {
        OrderItem orderItem = this.findById(id);
        orderItem.setEnabled(Boolean.FALSE);
        this.orderItemRepository.save(orderItem);
    }
}