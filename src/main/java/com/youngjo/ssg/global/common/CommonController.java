package com.youngjo.ssg.global.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CommonController {
    @RequestMapping("/")
    public String mainPage() {
        return "/index.html";
    }
}
