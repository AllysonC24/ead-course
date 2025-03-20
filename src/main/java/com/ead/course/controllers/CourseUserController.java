package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionRecordDTO;
import com.ead.course.dtos.UserRecordDTO;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CourseUserController {

    final AuthUserClient authUserClient;
    final CourseService courseService;
    final CourseUserService courseUserService;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserRecordDTO>> getAllUsersByCourse(@PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                                   @PathVariable(value = "courseId") UUID courseId){
        return ResponseEntity.status(HttpStatus.OK).body(authUserClient.getAllUsersByCourse(pageable, courseId));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                               @RequestBody @Valid SubscriptionRecordDTO subscritionRecordDTO){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if(courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscritionRecordDTO.userId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists");
        }

        ResponseEntity<UserRecordDTO> responseUser = authUserClient.getOneUserById(subscritionRecordDTO.userId());
        if(responseUser.getBody().userStatus().equals(UserStatus.BLOCKED)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: User is blocked");
        }

        CourseUserModel courseUserModel = courseUserService.saveAndSendSubscriptionUserInCourse(
                courseModelOptional.get().convertToCourseUserModel(subscritionRecordDTO.userId())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }
}
