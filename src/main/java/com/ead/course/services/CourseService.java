package com.ead.course.services;

import com.ead.course.dtos.CourseRecordDTO;
import com.ead.course.models.CourseModel;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseModel save(CourseRecordDTO courseRecordDTO);

    boolean existsByName(String name);

    List<CourseModel> findAll();

    Optional<CourseModel> findById(UUID courseId);

    CourseModel update(CourseModel courseModel, CourseRecordDTO courseRecordDTO);
}
