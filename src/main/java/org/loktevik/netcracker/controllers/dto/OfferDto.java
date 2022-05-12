package org.loktevik.netcracker.controllers.dto;

import lombok.Data;

@Data
public class OfferDto {
    private Long id;
    private String name;
    private Double price;
    private String paidType;
    private String category;
    private CharacteristicDto[] characteristics;
}