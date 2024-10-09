package com.sparrow.Finance.dto;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeMasterSetupInputsDTO {
	
	 private Long feeMasterSetupId;
	 private Optional<String> batchYear;
	 private String feeType;
	 private String feeGroupTitle;
	 private List<Long> courseGroups;
     private List<StudentCategoryDTO> studentCategory;
     private Boolean isNew;
     private Boolean isExisting;
     private Boolean isApplicableToAllYears;
     
     private List<FeeMasterDetailsInputsDTO> feeMasterDetailsInputsDTO;
     

}
