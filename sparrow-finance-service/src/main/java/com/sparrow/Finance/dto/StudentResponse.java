package com.sparrow.Finance.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class StudentResponse {
	private int code;
    private String message;
	 @JsonProperty("data")
	    private List<Student> students;

	    public List<Student> getStudents() {
	        return students;
	    }

	    public void setStudents(List<Student> students) {
	        this.students = students;
	    }

	    public static class Student {
	        @JsonProperty("studentId")
	        private Long studentId;

	        public Long getStudentId() {
	            return studentId;
	        }

	        public void setStudentId(Long studentId) {
	            this.studentId = studentId;
	        }
	    }

}
