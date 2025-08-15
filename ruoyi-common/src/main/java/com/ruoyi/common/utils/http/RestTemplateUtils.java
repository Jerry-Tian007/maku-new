package com.ruoyi.common.utils.http;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtils {

    public static JSONObject post(JSONObject param, String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONObject> entity = new HttpEntity<>(param, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, entity, String.class);
        return JSONUtil.parseObj(stringResponseEntity.getBody());
    }

    public static JSONObject post(JSONArray param, String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<JSONArray> entity = new HttpEntity<>(param, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, entity, String.class);
        return JSONUtil.parseObj(stringResponseEntity.getBody());
    }

}
