package org.loktevik.netcracker.controllers.dto;

import lombok.Data;

@Data
public class OffersResponseBody {
    private OfferDto[] offers;
    private CategoryDto[] categories;
}
