package com.teg.analysis.service;


import java.util.List;

public interface AnalysisService {

    /**
     * 根据文件解析需要的数据
     *
     * @param filePath
     * @return
     */
    List analysis(String filePath) throws InterruptedException;

}
