package com.example.demo;

import com.example.demo.model.analysis.AnalysisResultDTO;
import com.example.demo.service.AnalysisService;
import com.example.demo.service.ExcelService;
import com.example.demo.service.JIRAService;
import com.example.demo.service.TaiYueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

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
    void testJIRAUrl() {
        String orderNo = "004-20200624-0011";
        jiraService.post2JIRA(orderNo, jiraCookie, jiraToken);
    }

    @Test
    void testTYSMS() {
        taiYueService.post2TY4sms();
    }

    @Test
    void testTYLogin() {
        String validNo = "873014";
        taiYueService.login(validNo);
    }



    @Test
    void testTyUrl() {
        String orderNo = "004-20200703-0024";
        Map<String, String> tyInfo = taiYueService.post2TYInfo(orderNo, tyCookie);
        taiYueService.post2TYDetail(tyInfo.get("baseSchema"), tyInfo.get("baseId"), tyCookie);
    }


    @Test
    void testReadExcel() {
        LocalDateTime beginTime = LocalDateTime.now();
        Map<Integer, Map<Integer, Object>> result = excelService.readExcelFromPath(excelRead);
        Long opetime = Duration.between(beginTime, LocalDateTime.now()).toMillis();
        System.out.println("解析文件花费时间（毫秒）：" + opetime);
    }

}
