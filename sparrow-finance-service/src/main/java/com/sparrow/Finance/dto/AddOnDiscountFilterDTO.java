package com.sparrow.Finance.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddOnDiscountFilterDTO {
	private Long instituteId;
	private Long courseId;
	private String startDate;
	private String endDate;
	private Long feeTypeId;
	private Long FeeHeadId;
	

}
