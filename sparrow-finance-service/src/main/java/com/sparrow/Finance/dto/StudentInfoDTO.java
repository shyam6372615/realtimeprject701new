package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentInfoDTO {
	
	private Long studentId;
    private String studentEnrollmentNo;
    private String studentName;
    private String studentRollNo;
    private Long instituteId;
    private String instituteName;
    private String studentProfilePic;
    private Long libraryId;
    private String libraryName;
    private String email;
    private String phoneNo;
    private String studentPresentAddress;
    private String studentPermanentAddress;
    private String section;
    private String className;
    private String profieDiscountId;

}
