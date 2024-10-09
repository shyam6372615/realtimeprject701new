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
public class CashierDTO {
    private Long accConfigId;
    private String accessType;

    private List<cashierMappingDTO> cashierMappingDTO;


}
