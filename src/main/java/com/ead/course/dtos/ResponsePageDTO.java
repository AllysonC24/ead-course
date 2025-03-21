package com.ead.course.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsePageDTO<T> extends PageImpl<T> {

    private final PageMetadata page;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ResponsePageDTO(@JsonProperty("content") List<T> content,
                           @JsonProperty("page") PageMetadata page) {
        super(content, PageRequest.of(page.number, page.size), page.totalElements);
        this.page = page;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageMetadata {

        private final int size;
        private final long totalElements;
        private final int totalPages;
        private final int number;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public PageMetadata(@JsonProperty("size") int size,
                            @JsonProperty("totalElements") long totalElements,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("number") int number){
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.number = number;
        }

    }
}
