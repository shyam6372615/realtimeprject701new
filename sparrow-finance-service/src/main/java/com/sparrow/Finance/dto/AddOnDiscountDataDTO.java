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
public class AddOnDiscountDataDTO {
	
	private Long id;
	private Long fkStudentId;
	private Long userId;
    private Long fkInstituteId;
    private List<AddOnDiscountDTO> addOnDiscountList;
    

}
