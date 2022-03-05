package com.kufinder.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
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
        Elements rows = Jsoup.connect(url).get().select("tbody").get(0).select("tr");

        for (Element row : rows) {
            Elements infoCells = row.select("td");

            lectureRepository.save(Lecture.builder()
                .id(Integer.valueOf(infoCells.get(3).text()))
                .title(infoCells.get(4).text())
                .professor(infoCells.get(9).text())
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

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void updateCount() {
        HttpClient client = HttpClient.newHttpClient();

        List<CompletableFuture<Void>> resultList = lectureRepository.findAll().stream()
            .map(lecture -> {
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://kupis.konkuk.ac.kr/sugang/acd/cour/aply/CourInwonInqTime.jsp?ltYy=2022&ltShtm=B01011&sbjtId=" + lecture.getId()))
                    .build();
                return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        Elements countCells = Jsoup.parse(response.body()).select("td");
                        lecture.update(Integer.valueOf(countCells.get(3).text()), Integer.valueOf(countCells.get(5).text()));
                    });
            })
            .collect(Collectors.toList());

        resultList.forEach(CompletableFuture::join);
    }
}
