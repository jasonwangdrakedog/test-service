package com.teg.analysis;


import com.google.common.collect.Lists;
import com.teg.analysis.model.DTO.AnalysisResultDTO;
import com.teg.analysis.model.DTO.UserDTO;
import com.teg.analysis.model.REQ.UserREQ;
import com.teg.analysis.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
class AnalysisApplicationTests {

    @Value("${ty.cookie}")
    private String tyCookie;

    @Value("${jira.cookie}")
    private String jiraCookie;


    @Value("${jira.token}")
    private String jiraToken;

    @Value("${excel.read}")
    private String excelRead;


    @Resource
    private TaiYueService taiYueService;
    @Resource
    private JIRAService jiraService;
    @Resource
    private ExcelService excelService;
    @Resource(name = "taiYueAnalysisService")
    private AnalysisService analysisService;


    @Test
    void finalTest() throws Exception {
        LocalDateTime beginTime = LocalDateTime.now();
        List<AnalysisResultDTO> list = analysisService.analysis(excelRead);
        Long opetime = Duration.between(beginTime, LocalDateTime.now()).toMillis();
        System.out.println("解析文件花费时间（毫秒）：" + opetime);
        excelService.exportAnalysisResult(list);
        System.out.println();
    }


    @Test
    void testJIRAUrl() throws URISyntaxException {
        String orderNo = "004-20200624-0011";
        jiraService.get2JIRA(orderNo);
    }


    @Test
    void testTyUrl() throws URISyntaxException {
        String orderNo = "004-20200703-0024";
        Map<String, String> tyInfo = taiYueService.post2TYInfo(orderNo);
        taiYueService.get2TYDetail(tyInfo.get("baseSchema"), tyInfo.get("baseId"));
    }


    @Test
    void testReadExcel() {
        LocalDateTime beginTime = LocalDateTime.now();
        Map<Integer, Map<Integer, Object>> result = excelService.readExcelFromPath(excelRead);
        Long opetime = Duration.between(beginTime, LocalDateTime.now()).toMillis();
        System.out.println("解析文件花费时间（毫秒）：" + opetime);
    }

    @Resource
    DemoService demoService;

    @Test
    void testCreateUser() {
        UserREQ u = new UserREQ();
        u.setBirthday("1992-02-14");
        u.setHeight(180);
        u.setUserName("任盈64盈");
        u.setSex(1);
        u.setUserAddress("黑木崖");
        demoService.createUser(u);
    }

    @Test
    void testUpdateUser() {
        UserREQ u = new UserREQ();
        u.setUserId(8l);
        u.setBirthday("1995-03-14");
        u.setWeight(80);
        u.setUserName("欧阳锋33");
        u.setUserAddress("白驼山");
        u.setProvince("江苏");
        u.setSex(0);
        System.out.println(demoService.updateUser(u));
    }


    @Test
    void testBatchCreateUser() {
        UserREQ u1 = new UserREQ();
        u1.setBirthday("1957-02-14");
        u1.setHeight(181);
        u1.setUserName("任盈盈2");
        u1.setSex(0);
        u1.setUserAddress("黑木崖5");

        UserREQ u2 = new UserREQ();
        u2.setBirthday("1958-02-14");
        u2.setHeight(182);
        u2.setUserName("任盈盈3");
        u2.setSex(1);
        u2.setUserAddress("黑木崖6");
        List<UserREQ> userREQList = Lists.newArrayList();
        userREQList.add(u1);
        userREQList.add(u2);
        demoService.batchCreateUser(userREQList);
    }

    @Test
    void testQuery() {
        List<UserDTO> userDTOS = demoService.listUser();
        System.out.println();
    }

}
