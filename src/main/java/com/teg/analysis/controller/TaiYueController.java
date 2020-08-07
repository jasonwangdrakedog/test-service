package com.teg.analysis.controller;


import com.teg.analysis.aop.Log;
import com.teg.analysis.model.DTO.WorkOrderDTO;
import com.teg.analysis.model.REQ.WorkOrderQueryREQ;
import com.teg.analysis.model.common.Page;
import com.teg.analysis.service.TaiYueService;
import com.teg.analysis.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("ty")
public class TaiYueController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TaiYueController.class);


    @Resource
    private TaiYueService taiYueService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    @Log(description = "获取工单列表")
    public Page<WorkOrderDTO> listWorkOrder(WorkOrderQueryREQ workOrderQueryREQ) {
        List<WorkOrderDTO> list = taiYueService.findWorkOrder(workOrderQueryREQ);
        Page<WorkOrderDTO> page = PageUtil.success(list);
        return page;
    }
}
