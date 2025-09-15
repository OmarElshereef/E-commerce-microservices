package com.OmarElshereef.order_service.DTO;

import com.OmarElshereef.order_service.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Long userId;

    private List<OrderItemDto> orderItemsRequests;

}
