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
    private Integer currentCount;
    private Integer limitCount;

    @Builder
    public Lecture(Integer id, String title, String professor, Integer currentCount, Integer limitCount) {
        this.id = id;
        this.title = title;
        this.professor = professor;
        this.currentCount = currentCount;
        this.limitCount = limitCount;
    }
}
