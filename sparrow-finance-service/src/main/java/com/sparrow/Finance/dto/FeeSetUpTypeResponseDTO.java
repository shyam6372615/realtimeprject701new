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
public class FeeSetUpTypeResponseDTO {
	
	private Long feeSetupId;
	private Long instituteId;
	private String feeGroupTittle;
	private List<StudentCategoryDTO> studentCatyegory;
	private List<String> enrolledStatus;
	private Long appliedStudents;
	private List<FeeSetUpResponseDTO> feeSetupResponseDTO;
	

}
