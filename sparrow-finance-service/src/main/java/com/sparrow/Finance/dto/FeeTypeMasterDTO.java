package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeTypeMasterDTO {
	 private Long id;
	 private String feeType;
	 private Long instituteId;
     private Boolean isDefault=false;
     private Double tax;
	 private Double hsnOrSac;


}
