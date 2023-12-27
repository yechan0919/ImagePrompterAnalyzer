package com.example.ImagePrompterAnalyzer.controller;

import com.example.ImagePrompterAnalyzer.dto.InferenceRequestDTO;
import com.example.ImagePrompterAnalyzer.dto.InferenceResponseDTO;
import com.example.ImagePrompterAnalyzer.service.AnalyzerService;
import com.example.ImagePrompterAnalyzer.service.InferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@RestController
public class InferenceController {


    private final AnalyzerService analyzerService;
    private final InferenceService inferenceService;

    @PostMapping("/inference")
    public ResponseEntity<InferenceResponseDTO> doInference(@RequestBody InferenceRequestDTO request) {
        analyzerService.analyzeInferenceRequest(request);
        InferenceResponseDTO response = inferenceService.inference(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
