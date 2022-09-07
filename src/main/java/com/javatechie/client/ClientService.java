package com.javatechie.client;

import com.javatechie.dto.CourseRequestDTO;
import com.javatechie.dto.CourseResponseDTO;
import com.javatechie.dto.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientService {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8080/courses";

    //HTTP METHOD, URL, REQUEST & RESPONSE

    public ServiceResponse<CourseResponseDTO> addCourse(CourseRequestDTO courseRequestDTO) {
        return restTemplate.postForObject(BASE_URL, courseRequestDTO, ServiceResponse.class);
    }

    public ServiceResponse<List<CourseResponseDTO>> fetchAllCourse() {
        return restTemplate.getForObject(BASE_URL, ServiceResponse.class);
    }

    public ServiceResponse<CourseResponseDTO> getCourseById(int courseId) {
        return restTemplate.getForObject(BASE_URL + "/search/path/" + courseId, ServiceResponse.class);
    }

    public ServiceResponse<CourseResponseDTO> getCourseByIdRequestParam(Integer courseId) {
        Map<String, Integer> uriVariables = new HashMap<>();
        uriVariables.put("courseId", courseId);
        return restTemplate.getForObject(BASE_URL + "/search/path/" + courseId, ServiceResponse.class, uriVariables);
    }

    public void updateCourse(CourseRequestDTO courseRequestDTO, int courseId) {
        restTemplate.put(BASE_URL + "/" + courseId, courseRequestDTO);
    }

    public void deleteCourse(int courseId) {
        restTemplate.delete(BASE_URL + "/" + courseId);
    }
}
