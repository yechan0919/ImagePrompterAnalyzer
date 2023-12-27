package com.example.ImagePrompterAnalyzer.aop;

import com.example.ImagePrompterAnalyzer.exception.ApiKeyException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class HeaderValidationAspect {

    @Value("${app.api-key}")
    private String appApiKey;

    @Before("execution(* com.example.ImagePrompterAnalyzer.controller.*.*(..))")
    public void beforeControllerMethodExecution() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String apiKey = request.getHeader("API-Key");

        // API-Key가 존재하지 않으면 예외를 던지거나 로그를 남기는 등의 작업 수행 가능
        if (apiKey == null || apiKey.isEmpty() ) {
            throw new IllegalArgumentException("API-Key header is missing");
            // 또는 로그를 출력하거나 다른 처리를 수행할 수 있습니다.
            // logger.error("API-Key header is missing");
        }

        if(! appApiKey.equals(apiKey)){
            throw new ApiKeyException("Invalid API Key");
        }
//
//        // API-Key가 존재하면 추가적인 로직을 수행할 수 있습니다.
//        // 예: 토큰 검증, 사용자 인증 등
    }
}