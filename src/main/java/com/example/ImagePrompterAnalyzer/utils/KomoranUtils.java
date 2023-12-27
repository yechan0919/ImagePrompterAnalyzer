package com.example.ImagePrompterAnalyzer.utils;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;

public class KomoranUtils {
    private KomoranUtils(){}

    public static Komoran getInstance() {
        return KomoranInstance.instance;
    }

    private static class KomoranInstance {
        public static final Komoran instance = new Komoran(DEFAULT_MODEL.FULL);
    }
}