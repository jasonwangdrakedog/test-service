package com.example.demo.service;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.mapper.WorkOrderMapper;
import com.example.demo.model.DO.WorkOrderDO;
import com.example.demo.util.HtmlParserUtil;
import com.google.common.collect.Lists;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Service
public class TaiYueService {

    /**
     * 根据工单号查询对应的baseId
     */
    private final static String URL = "http://172.20.42.86:58088/ultrabpp/sheet/baseInfoQuery.action";

    /**
     * 泰岳工单搜索详情
     */
    private final static String DETAIL_URL = "http://172.20.42.86:58088/ultrabpp/ultrabpp/view.action?baseSchema={0}&baseID={1}&taskID=&mode=MODIFY";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private WorkOrderMapper workOrderMapper;

    private final static String SMS_URL = "http://172.20.41.180:58045/ucas/user/other/sendSmsMessage.htm?account=wangyuan&&passWord=6bb82c5b10de2eeac46c4a79d9fc9487f83768adc3c653501d0417c2d051abca1fb493f584e048e4564c9ca8d3ed07660d5d5d3df6578c26998c2c8c2ccdaf600321a7bcd359b9f4ff3637a32bf69c05be9c4128b00cf13422b5f720c18b5015ce94b9cb527d9ea60b9dd3e01c1b5ae66116fb53052008259787bd0b5d869276";

    //private final static String IMG_URL ="http://172.20.41.180:58045/ucas/style/assets/images/image_{0}.png";

    private final static String FI = "http://172.20.41.180:58045/ucas/login?service=" +
            "http%3A%2F%2F172.20.42.86%3A58088%2Fultrabpp%2Fj_acegi_cas_security_check%3Ft%3Dhttp%253A%252F%252F172.20.42.86%253A58088%252Fultr" +
            "abpp%252Fportal%252Flogin.action";

    public void post2TY4sms() {
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(SMS_URL, HttpMethod.GET, httpEntity, String.class);
        System.out.println(response.getBody());
    }


    public void login(String validNo) {
        HttpHeaders requestHeaders = new HttpHeaders();

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();


        paramMap.add("_eventId", "submit");
        paramMap.add("account", "wangyuan");
        paramMap.add("cipher", "6bb82c5b10de2eeac46c4a79d9fc9487f83768adc3c653501d0417c2d051abca1fb493f584e048e4564c9ca8d3ed07660d5d5d3df6578c26998c2c8c2ccdaf600321a7bcd359b9f4ff3637a32bf69c05be9c4128b00cf13422b5f720c18b5015ce94b9cb527d9ea60b9dd3e01c1b5ae66116fb53052008259787bd0b5d869276");
        paramMap.add("checkCode", validNo);
        paramMap.add("accountSwitch", "true");
        paramMap.add("chkPasmUserNamePswd", "on");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, requestHeaders);


        ResponseEntity<String> response2 = restTemplate.postForEntity(FI, httpEntity, String.class);
        System.out.println(response2.getBody());
    }


    public Map<String, String> post2TYInfo(String orderNo, String cookie) {
        // System.out.println("begin ty:" + orderNo);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List cookieList = Lists.newArrayList();
        cookieList.add("JSESSIONID=" + cookie);
        requestHeaders.put("Cookie", cookieList);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workSheetSerialnum", orderNo);
        //paramMap.add("searchUser", "搜索");


        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, requestHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(URL, httpEntity, String.class);
        //  System.out.println("result====================" + response.getBody());

        return HtmlParserUtil.parseTaiYue(response.getBody());

    }


    public Map<String, String> post2TYDetail(String baseSchema, String baseId, String cookie) {
        //   System.out.println("post2TYDetail begin:" + baseSchema + "-" + baseId);
        String url = MessageFormat.format(DETAIL_URL, baseSchema, baseId);
        HttpHeaders requestHeaders = new HttpHeaders();
        //  requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List cookieList = Lists.newArrayList();
        cookieList.add("JSESSIONID=" + cookie);
        requestHeaders.put("Cookie", cookieList);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return HtmlParserUtil.parseTaiYueDetail(response.getBody());
    }


    public String testSQL() {
        WorkOrderDO workOrderDO = new WorkOrderDO();
        workOrderDO.setWorkNo("888");
    //    workOrderMapper.insertWorkOrder(workOrderDO);
        List<WorkOrderDO> asdads = workOrderMapper.listWorkOrder("22");
        if (CollectionUtils.isEmpty(asdads)) {
            return "null";
        }
        return asdads.get(0).getWorkNo();
    }
}
