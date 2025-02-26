package com.ead.course.controllers;

import com.ead.course.dtos.CourseRecordDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("courses")
public class CourseController {

    final CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseRecordDTO courseRecordDTO){

        log.debug("POST saveCourse courseRecordDTO received {}", courseRecordDTO);
        if(courseService.existsByName(courseRecordDTO.name())){
            log.warn("Course name {} is Already Taken ", courseRecordDTO.name());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Course name already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.courseService.save(courseRecordDTO));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.findAll(spec, pageable));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId){
        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.findById(courseId).get());
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid CourseRecordDTO courseRecordDTO){
        log.debug("PUT updateCourse courseRecordDTO received {}", courseRecordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.update(this.courseService.findById(courseId).get(), courseRecordDTO));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId){
        log.debug("DELETE deleteCourse courseId received {}", courseId);
        this.courseService.delete(this.courseService.findById(courseId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully");
    }


}
