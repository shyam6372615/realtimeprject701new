package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinanceAccountsConfigDTO {
    private Long accConfigId;
    private Long fkFinanceAccSetupId;
    private String accessType;
    private String title;
    private Boolean status;
    private Boolean accessStatus;
    private Long instituteId;
    private Boolean isActive;
}