package com.javatechie.controller;

import com.javatechie.client.ClientService;
import com.javatechie.dto.CourseRequestDTO;
import com.javatechie.dto.CourseResponseDTO;
import com.javatechie.dto.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/addCourse")
    public ServiceResponse<CourseResponseDTO> addCourse(@RequestBody CourseRequestDTO courseRequestDTO){
        return clientService.addCourse(courseRequestDTO);
    }

    @GetMapping
    public ServiceResponse<List<CourseResponseDTO>> getCourses(){
        return clientService.fetchAllCourse();
    }

    @GetMapping("/path/{courseId}")
    public ServiceResponse<CourseResponseDTO> getCourse(@PathVariable int courseId){
        return clientService.getCourseById(courseId);
    }

    @GetMapping("/requestParam")
    public ServiceResponse<CourseResponseDTO> getCourseById(@RequestParam Integer courseId){
        return clientService.getCourseByIdRequestParam(courseId);
    }

    @PutMapping("/update/{courseId}")
    public ServiceResponse<CourseResponseDTO> updateCourse(@PathVariable int courseId,@RequestBody CourseRequestDTO courseRequestDTO){
        clientService.updateCourse(courseRequestDTO, courseId);
        return clientService.getCourseById(courseId);
    }

    @DeleteMapping("/delete/{courseId}")
    public void deleteCourse(@PathVariable int courseId){
        clientService.deleteCourse(courseId);
    }
}
