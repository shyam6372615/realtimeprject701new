package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashierDetailsDTO {
    private Long accSetupId;
    private Long empId;

    private Double openingBalance;

    private Boolean status=true;

    private Long instituteId;
   private  CashierDTO cashierDTO;
}
