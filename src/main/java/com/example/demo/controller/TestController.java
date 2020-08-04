package com.example.demo.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping(value = "rows",method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "ok";
    }

    public static void main(String[] args) {
        String s = "6bb82c5b10de2eeac46c4a79d9fc9487f83768adc3c653501d0417c2d051abca1fb493f584e048e4564c9ca8d3ed07660d5d5d3df6578c26998c2c8c2ccdaf600321a7bcd359b9f4ff3637a32bf69c05be9c4128b00cf13422b5f720c18b5015ce94b9cb527d9ea60b9dd3e01c1b5ae66116fb53052008259787bd0b5d869276";
        System.out.println(s.length());
    }
}
