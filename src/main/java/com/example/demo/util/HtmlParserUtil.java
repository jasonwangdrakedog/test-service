package com.example.demo.util;

import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

public class HtmlParserUtil {


    public static Map<String, String> parseTaiYue(String text) {
        Map<String, String> map = Maps.newHashMap();
        if (StringUtils.isEmpty(text)) {
            return map;
        }
        String split = text.substring(text.indexOf("onclick=\"openSheet(")).substring(0, text.substring(text.indexOf("onclick=\"openSheet(")).indexOf(")"));
        String[] splitArray = split.split("'");
        map.put("baseSchema", splitArray[1]);
        map.put("baseId", splitArray[3]);
        return map;

    }


    public static Map<String, String> parseTaiYueDetail(String text) {
        Map<String, String> map = Maps.newHashMap();
        if (StringUtils.isEmpty(text)) {
            return map;
        }
        List<Node> nodeList = Jsoup.parse(text).body().childNodes();
        nodeList.forEach(node -> {
            if (node instanceof FormElement && ((FormElement) node).id().equals("bpp_WorksheetForm")) {
                FormElement formElement = (FormElement) node;
                formElement.elements().forEach(e -> {
                    if (!StringUtils.isEmpty(e.val())) {
                        e.attributes().forEach(attribute -> {
                            if (attribute.getKey().equals("name")) {
                                map.put(attribute.getValue(), e.val());
                            }
                        });
                    }
                });
            }
        });
        return map;
    }
}
