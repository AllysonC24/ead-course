package com.ead.course.services.impl;

import com.ead.course.dtos.ModuleRecordDTO;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ModuleServiceImpl implements ModuleService {

    final ModuleRepository moduleRepository;
    final LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(ModuleModel moduleModel) {

        List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());

        if(!lessonModelList.isEmpty()){
            lessonRepository.deleteAll(lessonModelList);
        }
        moduleRepository.delete(moduleModel);
    }

    @Override
    public ModuleModel save(CourseModel courseModel, ModuleRecordDTO moduleRecordDTO) {

        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleRecordDTO, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModel);

        return this.moduleRepository.save(moduleModel);
    }

    @Override
    public List<ModuleModel> findAllModulesIntoCourse(UUID courseId) {
        return this.moduleRepository.findAllModulesIntoCourse(courseId);
    }

    @Override
    public Optional<ModuleModel> findModuleIntoCourse(UUID moduleId, UUID courseId) {

        var moduleModelOptional = this.moduleRepository.findModuleIntoCourse(moduleId, courseId);

        if(moduleModelOptional.isEmpty()){
            throw new NotFoundException("Error: Module not found");
        }

        return moduleModelOptional;
    }

    @Override
    public ModuleModel update(ModuleModel moduleModel, ModuleRecordDTO moduleRecordDTO) {

        BeanUtils.copyProperties(moduleRecordDTO, moduleModel);
        return this.moduleRepository.save(moduleModel);
    }

    @Override
    public Optional<ModuleModel> findById(UUID moduleId) {

        var moduleModelOptional = this.moduleRepository.findById(moduleId);

        if(moduleModelOptional.isEmpty()){
            throw new NotFoundException("Error: Module not found");
        }

        return moduleModelOptional;
    }

    @Override
    public Page<ModuleModel> findAllModulesIntoCourse(Specification<ModuleModel> spec, Pageable pageable) {
        return this.moduleRepository.findAll(spec, pageable);
    }
}
