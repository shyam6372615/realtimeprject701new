package com.sparrow.Finance.feignclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "studentClient", url = "http://164.52.201.171:9993")
public interface StudentFeignClient {

    @GetMapping("/api/students/getAllStudentsProfileByAcademicCourse/{instituteId}/{academicCourseId}")
    Map<String, Object> getStudentsByAcademicCourse(@PathVariable("instituteId") Long instituteId,
                                                    @PathVariable("academicCourseId") Long academicCourseId);
}
