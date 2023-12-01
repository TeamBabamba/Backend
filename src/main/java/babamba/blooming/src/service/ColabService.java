package babamba.blooming.src.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ColabService {

    @Value("${ai.url}")
    private String ENDPOINT;

    public List<String> getPlantAnalytics(String imgUrl) {
        // 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // requestBody 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("image_url", imgUrl);

        // 헤더와 바디 묶어서 엔티티로 만들기
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // RestTemplate으로 post 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, requestEntity, Map.class);

        Arrays.asList(response.getBody().get("plantName"), response.getBody().get("disease"));

        return Arrays.asList(response.getBody().get("plantName").toString(), response.getBody().get("disease").toString());
    }

}
