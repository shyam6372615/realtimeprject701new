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
public class ProfileDiscountGetDTO {
	
	private Long profileDiscountId;
	private Long instituteId;
	private String feeProfileTittle;
	private String description;
	private Double wavierAmount;
	private List<StudentCourseDTO> studentInfoDTO;

}
