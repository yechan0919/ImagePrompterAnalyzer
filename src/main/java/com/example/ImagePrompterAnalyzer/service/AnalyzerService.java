package com.example.ImagePrompterAnalyzer.service;

import com.example.ImagePrompterAnalyzer.dto.InferenceRequestDTO;
import com.example.ImagePrompterAnalyzer.utils.KomoranUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnalyzerService {

    public void analyzeInferenceRequest(InferenceRequestDTO request){
        Map<String, List<String>> promptAnalyzed = analyzeEnglish(request.getPrompt());
        Map<String, List<String>> negativePromptAnalyzed = analyzeEnglish(request.getNegative_prompt());

        if(! promptAnalyzed.get("NN").isEmpty()){
            log.info("prompted Noun: " + String.join(", ", promptAnalyzed.get("NN")));
        }
        if(! promptAnalyzed.get("JJ").isEmpty()){
            log.info("prompted Adjective: " + String.join(", ", promptAnalyzed.get("JJ")) );
        }

        if(negativePromptAnalyzed.get("NN") != null && ! negativePromptAnalyzed.get("NN").isEmpty()){
            log.info("negative prompted Noun: " + String.join(", ", negativePromptAnalyzed.get("NN")) );
        }
        if(negativePromptAnalyzed.get("JJ") != null && ! negativePromptAnalyzed.get("JJ").isEmpty()){
            log.info("negative prompted Adjective: " + String.join(", ", negativePromptAnalyzed.get("JJ")) );
        }
    }

    private Map<String, List<String>> analyzeEnglish(String text){
        if(StringUtils.isEmpty(text)){
            return Collections.emptyMap();
        }

        Map<String, List<String>> analyzed = new HashMap<>();

        // Stanford NLP 파이프라인 설정
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // 텍스트 처리를 위한 Stanford NLP 어노테이션 생성
        Annotation document = new Annotation(text);

        // 파이프라인 실행
        pipeline.annotate(document);

        // 문장에서 명사와 형용사 추출
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // 품사 정보를 가져오고, 명사와 형용사만 필터링
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                if (pos.startsWith("N") || pos.startsWith("J") ) {
                    analyzed.computeIfAbsent(pos, k -> new ArrayList<>()).add(token.originalText());
                }

            }
        }
        return analyzed;
    }

    // https://baboototo.tistory.com/32
    // http://kkma.snu.ac.kr/documents/?doc=postag
    public void analyzeKorean(String prompt){
        Komoran komoran = KomoranUtils.getInstance();
        KomoranResult analyzeResultList = komoran.analyze(prompt);

        List<Token> tokenList = analyzeResultList.getTokenList();

//        // 1. print each tokens by getTokenList()
//        log.info("==========print 'getTokenList()'==========");
//        for (Token token : tokenList) {
//            log.info(token.toString());
//            log.info(token.getMorph()+"/"+token.getPos()+"("+token.getBeginIndex()+","+token.getEndIndex()+")");
//
//        }

        // 2. print nouns
        log.info("==========print 'getNouns()'==========");
        log.info(analyzeResultList.getNouns().toString());

//        // 3. print analyzed result as pos-tagged text
//        log.info("==========print 'getPlainText()'==========");
//        log.info(analyzeResultList.getPlainText());
//
//        // 4. print analyzed result as list
//        log.info("==========print 'getList()'==========");
//        log.info(analyzeResultList.getList().toString());
//
//        // 5. print morphes with selected pos
//        log.info("==========print 'getMorphesByTags()'==========");
//        log.info(analyzeResultList.getMorphesByTags("NNG", "NNP", "NNB", "SL").toString());

        log.info("==========print '형용사'==========");
        log.info(analyzeResultList.getMorphesByTags("VA").toString());
    }
}
