package com.kufinder.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kufinder.service.LectureService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final LectureService lectureService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("lectureList", lectureService.findAll());
        return "index";
    }
}
