package com.ead.course.controllers;

import com.ead.course.dtos.ModuleRecordDTO;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ModuleController {

    final CourseService courseService;
    final ModuleService moduleService;

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid ModuleRecordDTO moduleRecordDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.moduleService.save(this.courseService.findById(courseId).get(), moduleRecordDTO));
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId,
                                                           SpecificationTemplate.ModuleSpec spec,
                                                           Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.moduleService.findAllModulesIntoCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "courseId") UUID courseId) {

        return ResponseEntity.status(HttpStatus.OK).body(this.moduleService.findModuleIntoCourse(moduleId, courseId).get());
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "courseId") UUID courseId,
                                               @RequestBody @Valid ModuleRecordDTO moduleRecordDTO) {

        return ResponseEntity.status(HttpStatus.OK).body(
                this.moduleService.update(this.moduleService.findModuleIntoCourse(moduleId, courseId).get(), moduleRecordDTO));
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "courseId") UUID courseId) {

        this.moduleService.delete(this.moduleService.findModuleIntoCourse(moduleId, courseId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully.");
    }
}
