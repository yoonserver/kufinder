package com.kufinder.domain.lecture;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Lecture {

    @Id
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

    @Builder
    public Lecture(Integer id, String title, String professor) {
        this.id = id;
        this.title = title;
        this.professor = professor;
    }

    public void updateFirstGradeCount(Integer firstGradeCurrentCount, Integer firstGradeLimitCount) {
        this.firstGradeCurrentCount = firstGradeCurrentCount;
        this.firstGradeLimitCount = firstGradeLimitCount;
    }

    public void updateSecondGradeCount(Integer secondGradeCurrentCount, Integer secondGradeLimitCount) {
        this.secondGradeCurrentCount = secondGradeCurrentCount;
        this.secondGradeLimitCount = secondGradeLimitCount;
    }

    public void updateThirdGradeCount(Integer thirdGradeCurrentCount, Integer thirdGradeLimitCount) {
        this.thirdGradeCurrentCount = thirdGradeCurrentCount;
        this.thirdGradeLimitCount = thirdGradeLimitCount;
    }

    public void updateFourthGradeCount(Integer fourthGradeCurrentCount, Integer fourthGradeLimitCount) {
        this.fourthGradeCurrentCount = fourthGradeCurrentCount;
        this.fourthGradeLimitCount = fourthGradeLimitCount;
    }

    public void updateAllGradeCount(Integer allGradeCurrentCount, Integer allGradeLimitCount) {
        this.allGradeCurrentCount = allGradeCurrentCount;
        this.allGradeLimitCount = allGradeLimitCount;
    }
}
