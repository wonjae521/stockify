package com.stock.stockify.domain.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/admin")
    public String adminTest() {
        return "관리자 전용 페이지입니다.";
    }

    @GetMapping("/staff")
    public String staffTest() {
        return "직원 전용 페이지입니다.";
    }

    @GetMapping("/ar")
    public String arTest() {
        return "AR 사용자 전용 페이지입니다.";
    }
}
