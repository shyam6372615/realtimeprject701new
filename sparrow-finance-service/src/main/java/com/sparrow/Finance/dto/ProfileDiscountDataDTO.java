package com.sparrow.Finance.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDiscountDataDTO {
	private Long profileDiscountId;
	private Long fkInstituteId;
	private String feeProfileTittle;
	private String description;
	private List<ProfileDiscountDTO> profileDiscounts;

}
