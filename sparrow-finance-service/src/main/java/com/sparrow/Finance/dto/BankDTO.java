package com.sparrow.Finance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankDTO {
    private Long accSetupId;

    @JsonIgnore
    private Long empId;

    private Double openingBalance;

    private Boolean status=true;

    private String bankName;

    private Long bankAccountNo;

    private String bankLogo ;

    private Long instituteId;

}
