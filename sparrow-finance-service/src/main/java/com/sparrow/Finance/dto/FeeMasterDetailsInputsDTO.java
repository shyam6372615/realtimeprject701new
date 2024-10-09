package com.sparrow.Finance.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeMasterDetailsInputsDTO {
	 private Long feeMasterDetailsId;
	 private Long feeHeadId;
	 private String feeHeadName;
	 private Float toKm;
	 private Float fromKm;
	 private Double feeAmount;
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	 private LocalDate duePaymentDay;
	 private Boolean isLateFeeApplicable;
	 private String occurance;
	 private String lateFeeType;
	 private Double tax;
	 private Double lateFeeAmountOrRate;
	 private Boolean isTerminateFeeApplicable=false;


}
