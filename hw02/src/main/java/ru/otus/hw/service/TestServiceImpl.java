package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String INVALID_ANSWER_NUMBER_TEMPLATE = "The answer is incorrect. " +
            "The answer should be a number from %s to %s.";

    private final IOService ioService;

    private final QuestionDao questionDao;

//    @Override
//    public void executeTest() {
//        ioService.printLine("");
//        ioService.printFormattedLine("Please answer the questions below%n");
//        try {
//            List<Question> questions = questionDao.findAll();
//            for (Question question : questions) {
//                ioService.printLine("Question: " + question.text());
//                for (Answer answer : question.answers()) {
//                    ioService.printFormattedLine("\t" + answer.text() + "\t" + answer.isCorrect(), "%s");
//                }
//            }
//        } catch (IOException e) {
//            throw new QuestionReadException(e.getMessage());
//        }
//    }

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var answersCount = question.answers().size();
            var answerIndex = ioService.readIntForRangeWithPrompt(
                    1,
                    answersCount,
                    question.text(),
                    String.format(INVALID_ANSWER_NUMBER_TEMPLATE, 1, answersCount)
            );
            var answer = question.answers().get(answerIndex - 1);

            var isAnswerValid = answer.isCorrect(); // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
