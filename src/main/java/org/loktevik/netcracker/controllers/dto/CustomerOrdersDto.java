package org.loktevik.netcracker.controllers.dto;

import lombok.Data;

@Data
public class CustomerOrdersDto {
    private OrderDto[] orders;
}
