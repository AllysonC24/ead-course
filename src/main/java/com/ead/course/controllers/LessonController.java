package com.ead.course.controllers;

import com.ead.course.dtos.LessonRecordDTO;
import com.ead.course.models.LessonModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@RestController
public class LessonController {

    final ModuleService moduleService;
    final LessonService lessonService;

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonRecordDTO lessonRecordDTO){
        log.debug("POST saveLesson lessonRecordDTO received {}", lessonRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.lessonService.save(this.moduleService.findById(moduleId).get(), lessonRecordDTO));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId,
                                                           SpecificationTemplate.LessonSpec spec,
                                                           Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.findAllLessonsIntoModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value = "lessonId") UUID lessonId,
                                               @PathVariable(value = "moduleId") UUID moduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.findLessonIntoModule(lessonId, moduleId));
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value = "lessonId") UUID lessonId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid LessonRecordDTO lessonRecordDTO){
        log.debug("PUT updateLesson lessonRecordDTO received {}", lessonRecordDTO);
        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.update(this.lessonService.findLessonIntoModule(lessonId, moduleId).get(), lessonRecordDTO));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "lessonId") UUID lessonId,
                                               @PathVariable(value = "moduleId") UUID moduleId) {
        log.debug("DELETE deleteLesson lessonId received {}", lessonId);
        log.debug("DELETE deleteLesson moduleId received {}", moduleId);
        this.lessonService.delete(this.lessonService.findLessonIntoModule(lessonId, moduleId).get());
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully");
    }

}
