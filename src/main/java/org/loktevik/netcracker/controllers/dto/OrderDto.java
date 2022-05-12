package org.loktevik.netcracker.controllers.dto;

import lombok.Data;

@Data
public class OrderDto {
    private Long orderId;
    private Long offerId;
    private String name;
    private String offerName;
    private String deliveryTime;
    private String status;
    private Integer amount;
    private Boolean isPaid;
    private Double fullPrice;
}