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
public class AccessDataDTO {
	private Long id;
	private String accessType;
	private Boolean accessStatus;
	private List<AccessDTO> accessDTO;

}
