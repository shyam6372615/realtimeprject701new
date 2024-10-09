package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeReasonDTO {

	private Long reasonId;
	private Long instituteId;
    private String type;
    private String reasonType;
    private Boolean isDefault;

}
