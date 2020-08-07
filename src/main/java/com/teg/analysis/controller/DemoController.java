package com.teg.analysis.controller;


import com.teg.analysis.model.DTO.UserDTO;
import com.teg.analysis.model.REQ.UserREQ;
import com.teg.analysis.model.common.Page;
import com.teg.analysis.model.common.Result;
import com.teg.analysis.service.DemoService;
import com.teg.analysis.util.PageUtil;
import com.teg.analysis.util.ResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private DemoService demoService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "ok";
    }


    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Result createUser(UserREQ userREQ) {
        demoService.createUser(userREQ);
        return ResultUtil.success();
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Page<UserDTO> listUser() {
       return PageUtil.success(demoService.listUser()) ;
    }

}
