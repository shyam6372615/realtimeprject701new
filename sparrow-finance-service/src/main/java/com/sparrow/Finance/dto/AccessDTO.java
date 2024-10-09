package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessDTO {
	private Long id;
	private String title;
    private Boolean status;

}
