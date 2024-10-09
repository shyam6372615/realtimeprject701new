package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FeeDiscountDTO {

	private Long feeDiscountHeadId;
	private Long fkInstituteId;
	private String discountHead;
	private Boolean isDefault;
	private Boolean isActive;
}
