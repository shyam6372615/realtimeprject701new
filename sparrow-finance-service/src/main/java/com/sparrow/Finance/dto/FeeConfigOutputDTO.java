package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeConfigOutputDTO {
	
	private Long id;
    private String keyward;
    private String type;
    private String value;
    private String feehead;
    private Boolean isDefault;

}
