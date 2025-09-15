package com.OmarElshereef.order_service.controller;


import com.OmarElshereef.order_service.DTO.OrderRequest;
import com.OmarElshereef.order_service.model.Order;
import com.OmarElshereef.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return "Order created";
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> findOrdersByUserId(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }


}
