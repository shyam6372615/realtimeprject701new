package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeHeadDetailsDTO {
	
	private Long feeHeadId;
	private String feeHeadName;
	private Double totalAmount;

}
