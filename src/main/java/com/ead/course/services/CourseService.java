package com.ead.course.services;

import com.ead.course.dtos.CourseRecordDTO;
import com.ead.course.models.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(CourseModel courseModel);

    CourseModel save(CourseRecordDTO courseRecordDTO);

    boolean existsByName(String name);

    Page<CourseModel> findAll(Specification<CourseModel> specification, Pageable pageable);

    Optional<CourseModel> findById(UUID courseId);

    CourseModel update(CourseModel courseModel, CourseRecordDTO courseRecordDTO);
}
