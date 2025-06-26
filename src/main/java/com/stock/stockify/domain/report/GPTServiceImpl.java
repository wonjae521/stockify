package com.stock.stockify.domain.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

// GPT 호출하는 클래스
@Slf4j
@Service
@RequiredArgsConstructor
public class GPTServiceImpl implements GPTService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-4o";

    @Override
    public String callChatGPT(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 메시지 포맷 정의
            Map<String, Object> message = Map.of(
                    "role", "user",
                    "content", prompt
            );

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", MODEL);
            requestBody.put("messages", List.of(message));
            requestBody.put("temperature", 0.7);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    OPENAI_API_URL,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            // 응답 메시지 추출
            Map responseBody = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");

            return messageObj.get("content").toString();

        } catch (Exception e) {
            log.error("GPT 호출 실패: ", e);
            return "GPT 응답 오류: " + e.getMessage();
        }
    }
}