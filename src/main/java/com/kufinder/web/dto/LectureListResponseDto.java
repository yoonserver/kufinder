package com.kufinder.web.dto;

import com.kufinder.domain.lecture.Lecture;
import lombok.Getter;

@Getter
public class LectureListResponseDto {
    private Integer id;
    private String title;
    private String professor;
    private Integer currentCount;
    private Integer limitCount;

    public LectureListResponseDto(Lecture entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.professor = entity.getTitle();
        this.currentCount = entity.getCurrentCount();
        this.limitCount = entity.getLimitCount();
    }
}
