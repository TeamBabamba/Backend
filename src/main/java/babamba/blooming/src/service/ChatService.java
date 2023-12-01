package babamba.blooming.src.service;

import babamba.blooming.src.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ChatService {

    private final UserRepository userRepository;

    @Value("${chatgpt.api-key}")
    private String API_KEY;
    private static final String ENDPOINT = "https://api.openai.com/v1/completions";

    public String getChatResponse(String prompt, float temperature, int maxTokens) {

        // 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        // requestBody 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model","gpt-3.5-turbo-instruct");
        requestBody.put("prompt", prompt);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);

        // 헤더와 바디 묶어서 엔티티로 만들기
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // RestTemplate으로 post 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, requestEntity, Map.class);

        return getGptResponseText(response.getBody()).trim();
    }

    // GPT 응답 추출 메서드
    public String getGptResponseText(Map response) {
        Object choices = response.get("choices");
        List<?> objects = convertObjectToList(choices);
        Object textObject = objects.get(0);

        ObjectMapper objectMapper = new ObjectMapper();

        // convert object to map
        Map<String, Object> map = objectMapper.convertValue(textObject, Map.class);

        return String.valueOf(map.get("text"));
    }

    // Object 객체를 List로 변환
    public List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }
}
