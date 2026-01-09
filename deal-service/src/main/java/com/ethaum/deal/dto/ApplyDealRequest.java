package com.ethaum.deal.dto;

import lombok.Data;

@Data
public class ApplyDealRequest {
    private Long dealId;
    private String enterpriseIndustry;
    private String enterpriseCompanySize;
}
