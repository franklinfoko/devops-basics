package com.bandesoft.medigorx;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DemoController {

    @GetMapping("/hello")
    public String hello(){
        return "Streamlining Medicine, Simplifying Lives, OK";
    }
}
