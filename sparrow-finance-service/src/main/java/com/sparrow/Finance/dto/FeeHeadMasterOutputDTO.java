package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeHeadMasterOutputDTO {
	private Long feeHeadId;
	 private String feeHeadName;
	 private Double tax;
	 private Long feeTypeId;
	 private String feeTypeName;
	 private Boolean isDefault=false;
	 private Long instituteId;

}
