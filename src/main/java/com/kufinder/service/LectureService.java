package com.kufinder.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kufinder.domain.lecture.Lecture;
import com.kufinder.domain.lecture.LectureRepository;
import com.kufinder.web.dto.LectureListResponseDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    @PostConstruct
    public void init() throws IOException {
        String url = "https://kupis.konkuk.ac.kr/sugang/acd/cour/time/SeoulTimetableInfo.jsp?ltYy=2022&ltShtm=B01011&sbjtId=&pobtDiv=B04054";
        Elements rows = Jsoup.connect(url).get().getElementsByTag("tbody").get(0).select("tr");

        for (Element row : rows) {
            Elements cells = row.getElementsByTag("td");
            lectureRepository.save(Lecture.builder()
                .id(Integer.valueOf(cells.get(3).text()))
                .title(cells.get(4).text())
                .professor(cells.get(9).text())
                .currentCount(0)
                .limitCount(0)
                .build()
            );
        }
    }

    @Transactional(readOnly = true)
    public List<LectureListResponseDto> findAll() {
        return lectureRepository.findAll().stream()
            .map(LectureListResponseDto::new)
            .collect(Collectors.toList());
    }
}
