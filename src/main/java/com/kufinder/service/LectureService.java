package com.kufinder.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kufinder.domain.lecture.LectureRepository;
import com.kufinder.web.dto.LectureListResponseDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    @Transactional(readOnly = true)
    public List<LectureListResponseDto> findAll() {
        return lectureRepository.findAll().stream()
            .map(LectureListResponseDto::new)
            .collect(Collectors.toList());
    }
}
