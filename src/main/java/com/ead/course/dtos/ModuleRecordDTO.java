package com.ead.course.dtos;

import jakarta.validation.constraints.NotBlank;

public record ModuleRecordDTO(@NotBlank(message = "Title is mandatory")
                              String title,

                              @NotBlank(message = "Description is mandatory")
                              String description) {
}
