package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseDTO {
	private Long studentId;
    private String stdFirstMiddleName;
    private String stdLastName;
    private String stdRollNo;
    private String stdUploadProfPic;
    private Long fkAcdmCourseId;
    private String academicCourseName;
    private Long programId;
    private String programName;
    private Long sectionId;
    private String sectionName;
    private String profileDiscount;
}
