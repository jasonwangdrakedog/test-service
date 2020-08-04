package com.example.demo.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Service
public class JIRAService {

    /**
     * eg: 1595990149375
     */
    private final static String URL = "http://jira.cmss.com/rest/quicksearch/1.0/productsearch/search?q={0}&_={1}";

    @Resource
    private RestTemplate restTemplate;

    public Map<String, String> post2JIRA(String orderNo, String cookie, String token) {
        Map<String, String> result = Maps.newHashMap();
        //  System.out.println("post2JIRA begin:" + orderNo + "-" + cookie + "-" + token);
        String url = MessageFormat.format(URL, orderNo, String.valueOf(System.currentTimeMillis()));
        HttpHeaders requestHeaders = new HttpHeaders();
        List cookieList = Lists.newArrayList();
        cookieList.add("JSESSIONID=" + cookie + ";atlassian.xsrf.token=" + token);
        requestHeaders.put("Cookie", cookieList);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        if (!StringUtils.isEmpty(response.getBody())) {
            List<JSONObject> jsonObjectList = JSONArray.parseArray(response.getBody(), JSONObject.class);
            jsonObjectList.forEach(jsonObject -> {
                if (jsonObject.getString("id").equals("quick-search-issues")) {
                    JSONArray items = jsonObject.getJSONArray("items");
                    if (!CollectionUtils.isEmpty(items)) {
                        JSONObject object = (JSONObject) items.get(0);
                        result.put("url", object.getString("url"));
                        result.put("title", object.getString("title"));
                    }
                }
            });
        }
        return result;
    }


}
