package com.OmarElshereef.order_service.components;

import com.OmarElshereef.order_service.DTO.Events.PaymentSuccessEvent;
import com.OmarElshereef.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderService orderService;

    @RabbitListener(queues = "payment.success")
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        log.info("Received PaymentSuccess event for order: {}", event.getOrderId());

        try {
            updateOrderStatus(event.getOrderId(), "PAID");
            log.info("Order {} marked as PAID", event.getOrderId());

        } catch (Exception e) {
            log.error("Failed to process PaymentSuccess for order: {}", event.getOrderId(), e);
            throw e;
        }
    }

    private void updateOrderStatus(Long orderId, String status) {
        orderService.updateOrderStatus(orderId, status);
    }
}
