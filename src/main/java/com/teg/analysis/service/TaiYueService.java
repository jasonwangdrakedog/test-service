package com.teg.analysis.service;


import com.teg.analysis.mapper.WorkOrderMapper;
import com.teg.analysis.model.DO.WorkOrderDO;
import com.teg.analysis.model.DTO.WorkOrderDTO;
import com.teg.analysis.model.REQ.WorkOrderQueryREQ;
import com.teg.analysis.util.HtmlParserUtil;
import com.teg.analysis.util.HttpUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Service
public class TaiYueService {

    @Value("${ty.cookie}")
    private String tyCookie;
    /**
     * 根据工单号查询对应的baseId
     */
    private final static String URL = "http://172.20.42.86:58088/ultrabpp/sheet/baseInfoQuery.action";

    /**
     * 泰岳工单搜索详情
     */
    private final static String DETAIL_URL = "http://172.20.42.86:58088/ultrabpp/ultrabpp/view.action?baseSchema={0}&baseID={1}&taskID=&mode=MODIFY";


    @Resource
    private WorkOrderMapper workOrderMapper;

    @Resource
    private HttpUtils httpUtils;


    public Map<String, String> post2TYInfo(String orderNo) throws URISyntaxException {
        // System.out.println("begin ty:" + orderNo);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List cookieList = Lists.newArrayList();
        cookieList.add("JSESSIONID=" + tyCookie);
        requestHeaders.put("Cookie", cookieList);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workSheetSerialnum", orderNo);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, requestHeaders);
        return HtmlParserUtil.parseTaiYue(httpUtils.postByArg(httpEntity, URL));

    }


    public Map<String, String> get2TYDetail(String baseSchema, String baseId) throws URISyntaxException {
        String url = MessageFormat.format(DETAIL_URL, baseSchema, baseId);
        HttpHeaders requestHeaders = new HttpHeaders();
        //  requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List cookieList = Lists.newArrayList();
        cookieList.add("JSESSIONID=" + tyCookie);
        requestHeaders.put("Cookie", cookieList);
      /*  String asdad = httpUtils.getByHeader(url, requestHeaders);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);*/
        return HtmlParserUtil.parseTaiYueDetail(httpUtils.getByArg(url, requestHeaders));
    }


    public List<WorkOrderDTO> findWorkOrder(WorkOrderQueryREQ workOrderQueryREQ) {
        List<WorkOrderDTO> dtoList = Lists.newArrayList();
        List<WorkOrderDO> doList = workOrderMapper.listWorkOrder(workOrderQueryREQ);
        if (!CollectionUtils.isEmpty(doList)) {
            doList.forEach(workOrderDO1 -> {
                WorkOrderDTO workOrderDTO = new WorkOrderDTO();
                BeanUtils.copyProperties(workOrderDO1, workOrderDTO);
                dtoList.add(workOrderDTO);
            });
        }
        return dtoList;
    }
}
