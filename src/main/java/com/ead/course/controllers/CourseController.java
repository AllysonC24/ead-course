package com.ead.course.controllers;

import com.ead.course.dtos.CourseRecordDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("courses")
public class CourseController {

    final CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseRecordDTO courseRecordDTO){

        if(courseService.existsByName(courseRecordDTO.name())){
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
        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.update(this.courseService.findById(courseId).get(), courseRecordDTO));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId){
        this.courseService.delete(this.courseService.findById(courseId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully");
    }


}
