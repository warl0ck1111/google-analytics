package com.example.ga4demo.shared.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class DataUtils {

    public Double getSimpleDoubleValue(double value) {
        BigDecimal bigDecimal = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public Long roundLongTo1000(Long volume) {
        if (volume != null) {
            return (volume + 999) / 1000 * 1000;//round to 1000
        } else {
            return null;
        }
    }

    public Long roundLongTo100(Long volume) {
        if (volume != null) {
            return (volume + 99) / 100 * 100;//round to 100
        } else {
            return null;
        }
    }

    public Long roundLongTo10(Long volume) {
        if (volume != null) {
            return (volume + 9) / 10 * 10;//round to 10
        } else {
            return null;
        }
    }


    public static int getNbOfWords(String label) {
        int total = 0;
        if (label != null) {
            total = label.split(" ").length;
        }
        return total;
    }

    public static int getNbOfWordsFromList(List<String> labels) {
        int totalWords = 0;
        if (labels != null) {
            for (String label : labels) {
                totalWords += getNbOfWords(label);
            }
        }
        return totalWords;
    }

    public String getFileExtension(String fileName) {
        try {
            String[] split = fileName.split("\\.");
            return split[split.length - 1];
        } catch (Exception e) {
            return "png";
        }
    }

}
