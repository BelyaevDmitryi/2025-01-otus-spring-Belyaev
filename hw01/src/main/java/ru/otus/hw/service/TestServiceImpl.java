package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        try {
            List<Question> questions = questionDao.findAll();
            for (Question question : questions) {
                ioService.printLine("Question: " + question.text());
                for (Answer answer : question.answers()) {
                    ioService.printFormattedLine("\t" + answer.text() + "\t" + answer.isCorrect(), "%s");
                }
            }
        } catch (IOException e) {
            throw new QuestionReadException(e.getMessage());
        }
    }
}
