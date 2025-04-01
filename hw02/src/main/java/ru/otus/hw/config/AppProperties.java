package ru.otus.hw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AppProperties implements TestConfig, TestFileNameProvider {

    private int rightAnswerCountToPass;

    private String testFileName;

    public AppProperties(@Value("${test.rightAnswersCountToPass}") int rightAnswerCountToPass,
                         @Value("${test.fileName}") String testFileName) {
        this.rightAnswerCountToPass = rightAnswerCountToPass;
        this.testFileName = testFileName;
    }

    @Override
    public int getRightAnswersCountToPass() {
        return rightAnswerCountToPass;
    }
}
