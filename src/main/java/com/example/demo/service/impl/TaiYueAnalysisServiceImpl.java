package com.example.demo.service.impl;

import com.example.demo.exception.BusinessException;
import com.example.demo.model.analysis.AnalysisResultDTO;
import com.example.demo.service.AnalysisService;
import com.example.demo.service.ExcelService;
import com.example.demo.service.JIRAService;
import com.example.demo.service.TaiYueService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("taiYueAnalysisService")
public class TaiYueAnalysisServiceImpl implements AnalysisService {

    @Value("${ty.cookie}")
    private String tyCookie;


    @Value("${jira.cookie}")
    private String jiraCookie;


    @Value("${jira.token}")
    private String jiraToken;

    @Resource
    private TaiYueService taiYueService;
    @Resource
    private JIRAService jiraService;
    @Resource
    private ExcelService excelService;

    /**
     * 对应列说明
     * 0-建单人 1-工单编号 2-工单选组类型 3-派单接口类型 4-工单状态 5-处理人 6-应用系统 7-标题
     * 8-故障描述 9-故障来源 10-所属资源池 11-处理组 12-驳回退回记录 13-派单组别 14-解决剩余时长
     * 15-超时组别 16-一线响应时长 17-处理时长(h) 18-故障清除时间 19-故障等级 20-故障分类 21-故障级别
     * 22-优先级 23-故障发生时间 24-告警入库时间 25-工单整体处理是否超时 26-超时环节 27-告警初始级别
     * 28-告警当前级别 29-告警首次发生时间 30-派单前告警最后发生时间 31-建单时间 32-T1派单时间 33-T2受理时间
     * 34-T2完成时间 35-T2处理时限 36-T3完成时间 37-T3处理时限 38-关闭时间 39-解决时限 40-一线预处理意见
     * 41-是否关联问题单 42-关联问题单 43-预处理结果 44-预处理过程 45-影响范围 46-是否影响业务 47-故障原因
     * 48-解决方案 49-根本原因解决方案 50-问题是否解决 51-关单说明 52-其他情况说明 53-作废记录 54-业务影响范围
     * 55-解决线条 56-一线累计处理时长 57-二线累计处理时长 58-二线故障是否及时解决 59-三线累计处理时长
     * 60-三线故障是否及时解决 61-超时记录 62-响应历时 63-响应是否超时 64-是否考核 65-免考核原因 66-是否新增优化监控
     * 67-是否故障 68-是否测试工单 69-处理时限 70-业务恢复时间 71-业务恢复时长
     *
     * @param filePath
     * @return
     */

    //  private AtomicInteger count = new AtomicInteger(0);
    @Override
    public List<AnalysisResultDTO> analysis(String filePath) throws InterruptedException {
        List<AnalysisResultDTO> analysisResultDTOS = Lists.newCopyOnWriteArrayList();
        Map<Integer, Map<Integer, Object>> excelData = excelService.readExcelFromPath(filePath);
        System.out.println("开始处理文件" + excelData.size() + "个");

        List<Map<Integer, Object>> list = excelData.entrySet().stream()
                .map(x -> x.getValue())
                .collect(Collectors.toList());
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

        list.forEach(integerObjectMap -> fixedThreadPool.execute(() -> {
            AnalysisResultDTO analysisResultDTO = fillDetail(integerObjectMap);
            analysisResultDTOS.add(analysisResultDTO);
        }));

        fixedThreadPool.shutdown();
        while (!fixedThreadPool.awaitTermination(1, TimeUnit.SECONDS)) {
        }
        System.out.println("over");
        return analysisResultDTOS;
    }

    private AnalysisResultDTO fillDetail(Map<Integer, Object> data) {

        Map<String, String> taiYueMap = taiYueService.post2TYInfo(String.valueOf(data.get(1)), tyCookie);
        if (CollectionUtils.isEmpty(taiYueMap)) {
            throw new BusinessException("泰岳工单未找到：" + data.get(1));
        }
        Map<String, String> tyDetailMap = taiYueService.post2TYDetail(taiYueMap.get("baseSchema"), taiYueMap.get("baseId"), tyCookie);
        if (CollectionUtils.isEmpty(tyDetailMap)) {
            throw new BusinessException("泰岳工单详情未找到：" + data.get(1));
        }


        AnalysisResultDTO analysisResultDTO = new AnalysisResultDTO();
        analysisResultDTO.setIsFault(fillIsFault(data));
        analysisResultDTO.setIsNormal(fillIsNormal(tyDetailMap));
        fillJIRAInfo(data, analysisResultDTO);
        fillPriority(data, analysisResultDTO);
        fillTeam(data, analysisResultDTO);

        analysisResultDTO.setTitle(String.valueOf(data.get(7)));
        analysisResultDTO.setTyDesc(tyDetailMap.get("malfunctionDes"));
        analysisResultDTO.setTyTitle(tyDetailMap.get("BaseSummary"));
        analysisResultDTO.setTyAnalysis(tyDetailMap.get("malfunctionCause"));
        //   analysisResultDTO.setTyReason(tyDetailMap.get("malfunctionCause"));
        analysisResultDTO.setTySolution(tyDetailMap.get("solveScheme"));
        analysisResultDTO.setWorkNo(String.valueOf(data.get(1)));
        return analysisResultDTO;
    }

    private void fillPriority(Map<Integer, Object> data, AnalysisResultDTO analysisResultDTO) {
        //这里excel 有一个优先级  泰岳里也有一个 目前先直接取excel里的
        if (!StringUtils.isEmpty(data.get(22))) {
            analysisResultDTO.setPriority(String.valueOf(data.get(22)));
        }
    }

    private void fillTeam(Map<Integer, Object> data, AnalysisResultDTO analysisResultDTO) {
        if (!StringUtils.isEmpty(data.get(11))) {
            String str = String.valueOf(data.get(11));
            if (str.indexOf("OP") >= 0) {
                analysisResultDTO.setTeam("BC-OP");
            } else if (str.indexOf("SDN") >= 0) {
                analysisResultDTO.setTeam("SDN");
            } else if (str.indexOf("EC") >= 0) {
                analysisResultDTO.setTeam("BC-EC");
            } else if (str.indexOf("EBS") >= 0 || str.indexOf("ONEST") >= 0 || str.indexOf("NFS") >= 0) {
                analysisResultDTO.setTeam("BC-SDS");
            } else if (str.indexOf("DW") >= 0) {
                analysisResultDTO.setTeam("BC-DW/BC-CLM");
            }
        }
    }


    private void fillJIRAInfo(Map<Integer, Object> data, AnalysisResultDTO analysisResultDTO) {
        if (!StringUtils.isEmpty(data.get(41)) && String.valueOf(data.get(41)).equals("是") &&
                !StringUtils.isEmpty(data.get(42))) {
            // 有关联问题单
            Map<String, String> jiraMap = jiraService.post2JIRA(String.valueOf(data.get(42)), jiraCookie, jiraToken);
            if (!CollectionUtils.isEmpty(jiraMap)) {
                analysisResultDTO.setJiraURL(jiraMap.get("url"));
            }
        }
    }

    private Boolean fillIsNormal(Map<String, String> tyDetailMap) {
        String pooltype = tyDetailMap.get("pooltype");
        if (!StringUtils.isEmpty(pooltype) && pooltype.equals("共性问题")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean fillIsFault(Map<Integer, Object> data) {
        if (!StringUtils.isEmpty(data.get(40)) && String.valueOf(data.get(40)).indexOf("误告警") >= 0
                && String.valueOf(data.get(40)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //一线预处理意见
        }
        if (!StringUtils.isEmpty(data.get(43)) && String.valueOf(data.get(43)).indexOf("误告警") >= 0
                && String.valueOf(data.get(43)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //预处理结果
        }
        if (!StringUtils.isEmpty(data.get(44)) && String.valueOf(data.get(44)).indexOf("误告警") >= 0
                && String.valueOf(data.get(44)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //预处理过程
        }
        if (!StringUtils.isEmpty(data.get(47)) && String.valueOf(data.get(47)).indexOf("误告警") >= 0
                && String.valueOf(data.get(47)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //故障原因
        }
        if (!StringUtils.isEmpty(data.get(48)) && String.valueOf(data.get(48)).indexOf("误告警") >= 0
                && String.valueOf(data.get(48)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //解决方案
        }
        if (!StringUtils.isEmpty(data.get(12)) && String.valueOf(data.get(12)).indexOf("误告警") >= 0
                && String.valueOf(data.get(12)).indexOf("错误告警") == -1) {
            return Boolean.TRUE;  //驳回退回记录
        }
        return Boolean.FALSE;
    }
}
