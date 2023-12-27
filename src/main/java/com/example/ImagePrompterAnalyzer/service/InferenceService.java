package com.example.ImagePrompterAnalyzer.service;

import com.example.ImagePrompterAnalyzer.dto.InferenceRequestDTO;
import com.example.ImagePrompterAnalyzer.dto.InferenceResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class InferenceService {

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public InferenceResponseDTO inference(InferenceRequestDTO request){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return restTemplate.exchange(
                kakaoApiUrl,
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                InferenceResponseDTO.class
        ).getBody();
    }


}
