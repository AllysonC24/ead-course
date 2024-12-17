package com.ead.course.services.impl;

import com.ead.course.dtos.CourseRecordDTO;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {

    final CourseRepository courseRepository;
    final LessonRepository lessonRepository;
    final ModuleRepository moduleRepository;


    //Metodo de deleção CASCADE ALL customizado
    @Transactional
    @Override
    public void delete(CourseModel courseModel) {

        List<ModuleModel> moduleModelList = this.moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());

        if(!moduleModelList.isEmpty()){

            for(ModuleModel moduleModel : moduleModelList){
                List<LessonModel> lessonModelList = this.lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());
                if(!lessonModelList.isEmpty()){
                    this.lessonRepository.deleteAll(lessonModelList);
                }
            }
            this.moduleRepository.deleteAll(moduleModelList);
        }
        this.courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseRecordDTO courseRecordDTO) {

        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseRecordDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return this.courseRepository.save(courseModel);
    }

    @Override
    public boolean existsByName(String name) {
        return this.courseRepository.existsByName(name);
    }

    @Override
    public List<CourseModel> findAll() {
        return this.courseRepository.findAll();
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {

        var courseModelOptional = this.courseRepository.findById(courseId);

        if(courseModelOptional.isEmpty()){
            throw new NotFoundException("Error: Course not found");
        }

        return courseModelOptional;
    }

    @Override
    public CourseModel update(CourseModel courseModel, CourseRecordDTO courseRecordDTO) {

        BeanUtils.copyProperties(courseRecordDTO, courseModel);
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return this.courseRepository.save(courseModel);
    }

}
