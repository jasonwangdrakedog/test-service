package com.teg.analysis.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author wangyuan
 * @date 2020/8/6 18:35
 */
@Component
@Slf4j
public class HttpUtils {

    @Resource
    private RestTemplate restTemplate;

    public String getByArg(String url, MultiValueMap<String, String> headers) throws URISyntaxException {
        headers.add("traceId", MDC.get("traceId"));
        URI uri = new URI(url);
        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);
        ResponseEntity<String> exchange = restTemplate.exchange(requestEntity, String.class);
        if (exchange.getStatusCode().equals(HttpStatus.OK)) {
            log.info("get http request success");
        }
        return exchange.getBody();
    }


    public String get(String url) throws URISyntaxException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        return getByArg(url, headers);
    }


    public String postByArg(HttpEntity<MultiValueMap<String, Object>> httpEntity, String url) throws URISyntaxException {
        HttpHeaders headers = httpEntity.getHeaders();
        headers.add("traceId", MDC.get("traceId"));
        URI uri = new URI(url);
        ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        if (exchange.getStatusCode().equals(HttpStatus.OK)) {
            log.info("post http request success");
        }
        return exchange.getBody();
    }

    public String post(String url) throws URISyntaxException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        return postByArg(httpEntity, url);
    }


}
