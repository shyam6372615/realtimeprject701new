package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultFeeSettingsConfigDTO {

    private Long id;

    private Long instituteId;

    private Boolean isAllowedUnselectOverdue = false;

    private Boolean isAllowedUnselectUpcomingOrCurrentDue = false;

    private Boolean isAllowedUnselectPaidAmount = false;

}
