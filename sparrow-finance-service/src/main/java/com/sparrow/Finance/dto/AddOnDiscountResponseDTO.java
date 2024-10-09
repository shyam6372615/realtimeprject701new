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
public class AddOnDiscountResponseDTO {
	private Long id;
	private String dateTime;
	private String studentName;
	private String studentImg;
	private Long studentId;
	private String className;
	private String section;
	private List<String> feeHeadName;
	private List<String> feeTypeName;
	private List<Double> discount;
	private String employeeName;
	private Long empId;
	private String empImg;
	private String studentRollNo;
	private List<String> roles;
	

}
