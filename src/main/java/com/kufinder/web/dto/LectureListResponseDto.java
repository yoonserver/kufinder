package com.kufinder.web.dto;

import com.kufinder.domain.lecture.Lecture;
import lombok.Getter;

@Getter
public class LectureListResponseDto {

    private Integer id;
    private String title;
    private String professor;

    private Integer firstGradeCurrentCount;
    private Integer firstGradeLimitCount;

    private Integer secondGradeCurrentCount;
    private Integer secondGradeLimitCount;

    private Integer thirdGradeCurrentCount;
    private Integer thirdGradeLimitCount;

    private Integer fourthGradeCurrentCount;
    private Integer fourthGradeLimitCount;

    private Integer allGradeCurrentCount;
    private Integer allGradeLimitCount;

    public LectureListResponseDto(Lecture entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.professor = entity.getTitle();
        this.firstGradeCurrentCount = entity.getFirstGradeCurrentCount();
        this.firstGradeLimitCount = entity.getFirstGradeLimitCount();
        this.secondGradeCurrentCount = entity.getSecondGradeCurrentCount();
        this.secondGradeLimitCount = entity.getSecondGradeLimitCount();
        this.thirdGradeCurrentCount = entity.getThirdGradeCurrentCount();
        this.thirdGradeLimitCount = entity.getThirdGradeLimitCount();
        this.fourthGradeCurrentCount = entity.getFourthGradeCurrentCount();
        this.fourthGradeLimitCount = entity.getFourthGradeLimitCount();
        this.allGradeCurrentCount = entity.getAllGradeCurrentCount();
        this.allGradeLimitCount = entity.getAllGradeLimitCount();
    }
}
