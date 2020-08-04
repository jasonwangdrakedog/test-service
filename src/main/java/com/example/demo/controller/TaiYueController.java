package com.example.demo.controller;


import com.example.demo.service.TaiYueService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("ty")
public class TaiYueController {


    @Resource
    private TaiYueService taiYueService;

    @RequestMapping("test")
    @ResponseBody
    public String test() {
        return taiYueService.testSQL();
    }
}
