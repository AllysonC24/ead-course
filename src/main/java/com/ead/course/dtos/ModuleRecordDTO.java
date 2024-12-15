package com.ead.course.dtos;

import jakarta.validation.constraints.NotBlank;

public record ModuleRecordDTO(@NotBlank
                              String title,

                              @NotBlank
                              String description) {
}
