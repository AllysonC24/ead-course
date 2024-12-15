package com.ead.course.services;

import com.ead.course.dtos.LessonRecordDTO;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {

    LessonModel save(ModuleModel moduleModel, LessonRecordDTO lessonRecordDTO);

    List<LessonModel> findAllLessonsIntoModule(UUID moduleId);

    Optional<LessonModel> findLessonIntoModule(UUID lessonId, UUID moduleId);

    void delete(LessonModel lessonModel);

    LessonModel update(LessonModel lessonModel, LessonRecordDTO lessonRecordDTO);
}
