package com.ead.course.services.impl;

import com.ead.course.dtos.LessonRecordDTO;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LessonServiceImpl implements LessonService {

    final LessonRepository lessonRepository;

    @Override
    public LessonModel save(ModuleModel moduleModel, LessonRecordDTO lessonRecordDTO) {

        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonRecordDTO, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModel);

        return this.lessonRepository.save(lessonModel);
    }

    @Override
    public List<LessonModel> findAllLessonsIntoModule(UUID moduleId) {
        return this.lessonRepository.findAllLessonsIntoModule(moduleId);
    }

    @Override
    public Optional<LessonModel> findLessonIntoModule(UUID lessonId, UUID moduleId) {

        var lessonModelOptional = this.lessonRepository.findLessonIntoModule(lessonId, moduleId);

        if(lessonModelOptional.isEmpty()){
            throw new NotFoundException("Error: Lesson not found");
        }

        return lessonModelOptional;
    }

    @Override
    public void delete(LessonModel lessonModel) {
        this.lessonRepository.delete(lessonModel);
    }

    @Override
    public LessonModel update(LessonModel lessonModel, LessonRecordDTO lessonRecordDTO) {

        BeanUtils.copyProperties(lessonRecordDTO, lessonModel);
        return this.lessonRepository.save(lessonModel);
    }

    @Override
    public Page<LessonModel> findAllLessonsIntoModule(Specification<LessonModel> spec, Pageable pageable) {
        return this.lessonRepository.findAll(spec, pageable);
    }
}
