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
public class StudentResponseDTO {
	   
	    private int code;
	    private String message;
	    private List<StudentCourseDTO> data;


}
