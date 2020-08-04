package com.example.demo.controller;


import com.example.demo.model.DO.WorkOrderDO;
import com.example.demo.service.TaiYueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("ty")
public class TaiYueController {


    @Resource
    private TaiYueService taiYueService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public WorkOrderDO test() {
        return taiYueService.testSQL();
    }
}
