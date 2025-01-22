package sharingcalender.calender.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "routing test";
    }

    @GetMapping("/addTest")
    public String addTest() {
        return "routing ADD test";
    }
}
