package roey.com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/status")
    public String getStatus() {
        return "Controller init";
    }

}
