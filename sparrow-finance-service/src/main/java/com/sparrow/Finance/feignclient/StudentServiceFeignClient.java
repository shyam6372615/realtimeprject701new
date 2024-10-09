package com.sparrow.Finance.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sparrow.Finance.dto.StudentResponseDTO;

@FeignClient(name = "studentServiceClient", url = "http://164.52.201.171:9993")
public interface StudentServiceFeignClient {

    @GetMapping("/api/students/getStudentsByInstituteAndCourse")
    StudentResponseDTO getStudentsByInstituteAndCourse(@RequestParam("instituteId") Long instituteId,
                                                           @RequestParam("courseId") Long courseId);
}