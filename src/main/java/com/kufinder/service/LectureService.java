package com.kufinder.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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
        String url = "https://kupis.konkuk.ac.kr/sugang/acd/cour/time/SeoulTimetableInfo.jsp?ltYy=2022&ltShtm=B01011&pobtDiv=B04054";
        Elements rows = Jsoup.connect(url).get().select("tbody").get(0).select("tr");

        for (Element row : rows) {
            Elements infoCells = row.select("td");

            lectureRepository.save(Lecture.builder()
                .id(Integer.valueOf(infoCells.get(3).text()))
                .title(infoCells.get(4).text())
                .professor(infoCells.get(9).text())
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
        String firstGradeUrl = "https://kupis.konkuk.ac.kr/sugang/acd/cour/aply/CourBasketInwonInq.jsp?ltYy=2022&ltShtm=B01011&promShyr=1&fg=B&sbjtId=";
        String secondGradeUrl = "https://kupis.konkuk.ac.kr/sugang/acd/cour/aply/CourBasketInwonInq.jsp?ltYy=2022&ltShtm=B01011&promShyr=2&fg=B&sbjtId=";
        String thirdGradeUrl = "https://kupis.konkuk.ac.kr/sugang/acd/cour/aply/CourBasketInwonInq.jsp?ltYy=2022&ltShtm=B01011&promShyr=3&fg=B&sbjtId=";
        String fourthGradeUrl = "https://kupis.konkuk.ac.kr/sugang/acd/cour/aply/CourBasketInwonInq.jsp?ltYy=2022&ltShtm=B01011&promShyr=4&fg=B&sbjtId=";
        String allGradeUrl = "https://kupis.konkuk.ac.kr/sugang/acd/cour/aply/CourInwonInqTime.jsp?ltYy=2022&ltShtm=B01011&sbjtId=";

        List<Lecture> lectureList = lectureRepository.findAll();
        List<CompletableFuture<Void>> resultList = new ArrayList<>();

        for (Lecture lecture : lectureList) {
            HttpClient client = HttpClient.newHttpClient();
            resultList.add(client.sendAsync(HttpRequest.newBuilder().uri(URI.create(firstGradeUrl + lecture.getId())).build(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    String firstGradeCount[] = Jsoup.parse(response.body()).select("td").get(1).text().split("/");
                    lecture.updateFirstGradeCount(Integer.valueOf(firstGradeCount[0].trim()), Integer.valueOf(firstGradeCount[1].trim()));
                }));
            resultList.add(client.sendAsync(HttpRequest.newBuilder().uri(URI.create(secondGradeUrl + lecture.getId())).build(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    String secondGradeCount[] = Jsoup.parse(response.body()).select("td").get(1).text().split("/");
                    lecture.updateSecondGradeCount(Integer.valueOf(secondGradeCount[0].trim()), Integer.valueOf(secondGradeCount[1].trim()));
                }));
            resultList.add(client.sendAsync(HttpRequest.newBuilder().uri(URI.create(thirdGradeUrl + lecture.getId())).build(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    String thirdGradeCount[] = Jsoup.parse(response.body()).select("td").get(1).text().split("/");
                    lecture.updateThirdGradeCount(Integer.valueOf(thirdGradeCount[0].trim()), Integer.valueOf(thirdGradeCount[1].trim()));
                }));
            resultList.add(client.sendAsync(HttpRequest.newBuilder().uri(URI.create(fourthGradeUrl + lecture.getId())).build(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    String fourthGradeCount[] = Jsoup.parse(response.body()).select("td").get(1).text().split("/");
                    lecture.updateFourthGradeCount(Integer.valueOf(fourthGradeCount[0].trim()), Integer.valueOf(fourthGradeCount[1].trim()));
                }));
            resultList.add(client.sendAsync(HttpRequest.newBuilder().uri(URI.create(allGradeUrl + lecture.getId())).build(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    Elements allGradeCount = Jsoup.parse(response.body()).select("td");
                    lecture.updateAllGradeCount(Integer.valueOf(allGradeCount.get(3).text()), Integer.valueOf(allGradeCount.get(5).text()));
                }));
        }

        resultList.forEach(CompletableFuture::join);
    }
}
