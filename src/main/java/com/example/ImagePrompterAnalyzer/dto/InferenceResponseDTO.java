package com.example.ImagePrompterAnalyzer.dto;

import lombok.Data;

import java.util.List;

@Data
public class InferenceResponseDTO {
    private String id;
    private String model_version;
    private List<Image> images;

    @Data
    public static class Image {
        private String id;
        private long seed;
        private String image;
    }
}


