package com.sparrow.Finance.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeSetUpResponseDTO {
	private Long feeDetailsId;
	private LocalDate dueDate;
	private String feeHeadName;
	private Double feeAmount;
	private Double tax;
	private Double totalAmount;
	private String occurance;
	private Double lateFeeAmountOrRate;
	private String lateFeeType;
	

}
