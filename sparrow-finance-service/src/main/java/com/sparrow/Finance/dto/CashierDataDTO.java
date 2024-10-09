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
public class CashierDataDTO {
	private Long accSetupId; 
	private Long instituteId;
	private Double openingBalance;
	private Boolean status;
    private Long empId;
    private List<AccessDataDTO> aceessData;


}
