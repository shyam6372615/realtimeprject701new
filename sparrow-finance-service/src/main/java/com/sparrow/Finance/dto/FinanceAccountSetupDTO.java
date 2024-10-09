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
public class FinanceAccountSetupDTO {
    private Long accSetupId;
    private Long empId;
    private Double openingBalance;
    private Boolean status;
    private String bankName;
    private Long bankAccountNo;
    private String bankLogo;
    private Long instituteId;
    private Boolean isActive;
    private List<FinanceAccountsConfigDTO> accountConfigs;
}