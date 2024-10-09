package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountResponseDTO {
	
	private Long discounId;
	private String discountType;
	private String feeHeadName;
	private String discountAmountType;
	private Double discountRate;
	private String disCountHeadname;

}
