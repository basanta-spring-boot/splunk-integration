package com.javatechie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.dao.CourseDao;
import com.javatechie.dto.CourseRequestDTO;
import com.javatechie.dto.CourseCount;
import com.javatechie.dto.CourseResponseDTO;
import com.javatechie.model.CourseEntity;
import com.javatechie.util.AppUtil;
import exception.CourseServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CourseService {

    Logger logger = LogManager.getLogger(CourseService.class);

    @Autowired
    private CourseDao courseDao;

    public CourseResponseDTO onboardNewCourse(CourseRequestDTO courseRequestDTO) {
        logger.info("onboardNewCourse() method started execution");
        logger.info("onboardNewCourse() service method request payload {}", printRequestResponse(courseRequestDTO));
        try {
            CourseEntity courseEntity = AppUtil.mapCourseDtoToEntity(courseRequestDTO);
            CourseEntity course = courseDao.save(courseEntity);
            logger.debug("course entity response {}", printRequestResponse(course));
            CourseResponseDTO courseResponseDTO = AppUtil.mapCourseEntityToDto(course);
            courseResponseDTO.setCourseUniqueCode(UUID.randomUUID().toString().split("-")[0]);
            logger.debug("onboardNewCourse service method response {}", printRequestResponse(courseResponseDTO));
            logger.info("onboardNewCourse() method ended execution");
            return courseResponseDTO;
        } catch (Exception ex) {
            logger.error("CourseService::onboardNewCourse exception occurs while persisting new course {}", ex.getMessage());
            throw new CourseServiceException("Exception occurs in method : onboardNewCourse");
        }
    }

    public List<CourseResponseDTO> viewAllCourses() {
        logger.info("viewAllCourses() method started execution");
        try {
            Iterable<CourseEntity> entities = courseDao.findAll();
            List<CourseResponseDTO> responseDTOS = StreamSupport.stream(entities.spliterator(), false)
                    .map(AppUtil::mapCourseEntityToDto)
                    .collect(Collectors.toList());
            logger.debug("viewAllCourses response from DB {}", printRequestResponse(responseDTOS));
            logger.info("viewAllCourses() method ended execution");
            return responseDTOS;
        } catch (Exception ex) {
            logger.error("exception occurs in onboardNewCourse() method {}", ex.getMessage());
            throw new CourseServiceException("Exception occurs in method : onboardNewCourse");
        }

    }

    public CourseResponseDTO findCourseById(Integer courseId) {
        logger.info("findCourseById() method started execution");
        CourseEntity courseEntity = courseDao.findById(courseId)
                .orElseThrow(() -> new CourseServiceException("courseId " + courseId + " doesn't exist"));
        CourseResponseDTO courseResponseDTO = AppUtil.mapCourseEntityToDto(courseEntity);
        logger.debug("findCourseById response from DB {}", printRequestResponse(courseResponseDTO));
        logger.info("findCourseById() method ended execution");
        return courseResponseDTO;
    }


    public void deleteCourse(int courseId) {
        logger.info("deleteCourse() method started execution");
        try {
            courseDao.deleteById(courseId);
        } catch (Exception ex) {
            logger.error("exception occurs in deleteCourse() method {}", ex.getMessage());
            throw new CourseServiceException("Exception occurs in method : deleteCourse"+ex.getMessage());
        }
        logger.info("deleteCourse() method ended execution");
    }

    public CourseResponseDTO updateCourse(CourseRequestDTO course, int courseId) {
        logger.info("updateCourse() method started execution");
        try {
            logger.debug("updateCourse service request body {} and id {}", printRequestResponse(course), courseId);
            CourseEntity courseEntity = courseDao.findById(courseId).orElseThrow(()->new RuntimeException("course not found with id "+courseId));
            courseEntity.setName(course.getName());
            courseEntity.setTrainerName(course.getTrainerName());
            courseEntity.setCourseType(course.getCourseType());
            courseEntity.setCourseFees(course.getCourseFees());
            courseEntity.setDuration(course.getDuration());
            courseEntity.setStartDate(course.getStartDate());
            courseEntity.setCertificationAvailable(course.isCertificationAvailable());
            CourseEntity updatedCourseEntity = courseDao.save(courseEntity);
            CourseResponseDTO courseResponseDTO = AppUtil.mapCourseEntityToDto(updatedCourseEntity);
            logger.debug("updateCourse service response  {} ", printRequestResponse(courseResponseDTO));
            logger.info("updateCourse() method ended execution");
            return courseResponseDTO;
        } catch (Exception ex) {
            logger.error("exception occurs in updateCourse() method {}", ex.getMessage());
            throw new CourseServiceException("Exception occurs in method : updateCourse "+ex.getMessage());
        }

    }

    public List<CourseCount> getCourseCountByType() {
        List<CourseCount> courseCounts = new ArrayList<>();
        Map<String, Long> courseCountMap = StreamSupport.stream(courseDao.findAll().spliterator(), false)
                .collect(Collectors
                        .groupingBy(CourseEntity::getCourseType,
                                Collectors.counting()));
        courseCountMap.forEach((k, v) -> courseCounts.add(new CourseCount(k, v)));
        return courseCounts;
    }


    private String printRequestResponse(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
