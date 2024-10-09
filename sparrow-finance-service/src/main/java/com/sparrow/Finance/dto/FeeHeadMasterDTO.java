package com.sparrow.Finance.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeHeadMasterDTO {
	
	 private Long feeHeadId;
	 private String feeHeadName;
	 private Long feeTypeId;
	 private Boolean isDefault=false;
	 private Long instituteId;


}
