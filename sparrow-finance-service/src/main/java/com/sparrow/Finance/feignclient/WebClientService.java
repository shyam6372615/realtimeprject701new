package com.sparrow.Finance.feignclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.sparrow.Finance.dto.ApiResponseDTO;
import com.sparrow.Finance.dto.CourseDetailsDTO;
import com.sparrow.Finance.dto.StudentInfoDTO;

import reactor.core.publisher.Flux;

@Service
public class WebClientService {
    
    @Autowired
    private WebClient.Builder webClientBuilder;

    public Flux<CourseDetailsDTO> getCoursesByInstituteId(Long instituteId) {
        String url = String.format("http://164.52.201.171:9996/sparrow/api/v1/platformsettings/distinct-courses/%d", instituteId);
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(CourseDetailsDTO.class);
    }
    
    
    public ApiResponseDTO getUserRolesWithPrivilege(Long instituteId) {
        String url = "http://164.52.201.171:9991/app/api/roles-permission/get/user/roles/with/previlege/" + instituteId;

        try {
            return webClientBuilder.build()  
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(ApiResponseDTO.class)
                    .block();  
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to fetch user roles and privileges", e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while calling the API", e);
        }
    }
    public StudentInfoDTO getStudentInfo(Long instituteId, Long studentId) {
        String url = String.format("http://164.52.201.171:9993/api/students/getstudentInfo/%d/%d", instituteId, studentId);
        try {
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(StudentInfoDTO.class)
                .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to fetch user roles and privileges", e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while calling the API", e);
        }
    }
    
    public Object getStudentIds(Long instituteId, Long academicCourseId) {
        return webClientBuilder.build()
        		.get()
                .uri("http://164.52.201.171:9993/api/students/getAllStudentsProfileByAcademicCourse/{instituteId}/{academicCourseId}?instituteId=%d&academicCourseId=%s", instituteId, academicCourseId)
                .retrieve()
                .bodyToMono(Object.class);
                
    }
    
}
