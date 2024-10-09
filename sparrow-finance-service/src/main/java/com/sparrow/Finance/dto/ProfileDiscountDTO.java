package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDiscountDTO {
	
	private Long feetypeId;
	private String feeTypeName;
	private String discountType;
	private Double rate;

}
