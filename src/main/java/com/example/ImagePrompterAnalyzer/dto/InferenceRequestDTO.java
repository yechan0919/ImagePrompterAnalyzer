package com.example.ImagePrompterAnalyzer.dto;

import lombok.Data;

@Data
public class InferenceRequestDTO {
    private String prompt;
    private String negative_prompt;
}
