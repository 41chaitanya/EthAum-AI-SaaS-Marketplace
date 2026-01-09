package com.ethaum.deal.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DealResponse {

    private Long id;
    private String title;
    private String status;
    private int durationDays;
    private int maxSlots;
}
