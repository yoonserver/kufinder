package com.kufinder.domian.lecture;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kufinder.domain.lecture.Lecture;
import com.kufinder.domain.lecture.LectureRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LectureRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Test
    public void 강의_저장_불러오기() {
        //given
        Integer id = 1000;
        String title = "title";
        String professor = "professor";

        lectureRepository.save(Lecture.builder()
            .id(id)
            .title(title)
            .professor(professor)
            .build()
        );

        //when
        List<Lecture> lectureList = lectureRepository.findAll();

        //then
        Lecture lecture = lectureList.get(0);
        assertThat(lecture.getId()).isEqualTo(id);
        assertThat(lecture.getTitle()).isEqualTo(title);
        assertThat(lecture.getProfessor()).isEqualTo(professor);
    }
}
