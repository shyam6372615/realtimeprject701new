package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentModeConfigDTO {
	
	private Long id;
    private String paymentMode;
    private String accountType;
    private Long instituteId;
    private Boolean isDefault=false;
    

}
