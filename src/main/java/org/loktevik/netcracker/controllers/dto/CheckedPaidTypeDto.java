package org.loktevik.netcracker.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckedPaidTypeDto {
    private String name;
    private boolean isChosen;
}
