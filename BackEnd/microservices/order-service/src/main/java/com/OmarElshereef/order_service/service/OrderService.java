package com.OmarElshereef.order_service.service;

import com.OmarElshereef.order_service.DTO.Events.OrderCreatedEvent;
import com.OmarElshereef.order_service.DTO.OrderItemDto;
import com.OmarElshereef.order_service.DTO.OrderRequest;
import com.OmarElshereef.order_service.model.Order;
import com.OmarElshereef.order_service.model.OrderItem;
import com.OmarElshereef.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setUserId(orderRequest.getUserId());

        List<OrderItem> items = orderRequest.getOrderItemsRequests()
                .stream()
                .map(dto -> mapToEntity(dto, order))
                .toList();

        double totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        order.setTotalAmount(totalAmount);
        order.setOrderItems(items);
        order.setStatus("placed");


        orderRepository.save(order);
        publishOrderCreated(
                order.getId(),
                orderRequest.getUserId(),
                orderRequest.getOrderItemsRequests(),
                totalAmount
        );
    }

    private void publishOrderCreated(Long orderId,Long userId,
                                     List<OrderItemDto> items, double total) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                orderId,
                userId,
                items,
                total,
                LocalDateTime.now()
        );

        try {
            rabbitTemplate.convertAndSend("order.events", "order.created", event);
            log.info(" Published OrderCreated event for order: {}", orderId);
        } catch (Exception e) {
            log.error(" Failed to publish OrderCreated event for order: {}", orderId, e);
            // TODO: Handle publish failure (maybe retry or save to outbox table)
        }
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    private OrderItem mapToEntity(OrderItemDto dto, Order order) {
        return OrderItem.builder()
                .productId(dto.getProductId()) // Changed from skuCode to productId
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .order(order)
                .build();
    }

    public void updateOrderStatus(Long orderId,String orderStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(orderStatus);
            orderRepository.save(order);
        }
        else  {
            log.error(" Failed to update Order Status for order: {}", orderId);
        }
    }
}