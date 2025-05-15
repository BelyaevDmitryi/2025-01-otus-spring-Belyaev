package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {
    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private LocalizedIOService localizedIOService;

    @Mock
    private QuestionDao questionDao;

    private List<Question> questions;

    @BeforeEach
    void setUp() {
        var question = new Question("Is there life on Mars?", List.of(
                new Answer("Science doesn't know this yet", true),
                new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                new Answer("Absolutely not", false)));

        questions = List.of(question);
        when(questionDao.findAll()).thenReturn(questions);
    }

    @DisplayName("Проверка корректности сохранения имени и фамилии студента")
    @Test
    void studentCorrectNameAndSurnameTest() {
        var student = new Student("Ivanov", "Ivan");

        var inputAnswer = 1;
        when(localizedIOService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(inputAnswer);

        var result = testService.executeTestFor(student);
        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getStudent().firstName()).isEqualTo(student.firstName());
        assertThat(result.getStudent().lastName()).isEqualTo(student.lastName());
    }

    @DisplayName("Проверка сохраности текста вопросов и ответов")
    @Test
    void correctQuestionsTest() {
        var student = new Student("Ivanov", "Ivan");

        var inputAnswer = 1;
        when(localizedIOService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(inputAnswer);
        var result = testService.executeTestFor(student);
        Question firstActualQuestionInTestResult = result.getAnsweredQuestions().get(0);
        Question firstExpectedQuestion = questions.get(0);
        assertThat(firstActualQuestionInTestResult.text()).isEqualTo(firstExpectedQuestion.text());
        assertThat(firstActualQuestionInTestResult.answers().size()).isEqualTo(firstExpectedQuestion.answers().size());
        assertThat(firstActualQuestionInTestResult.answers()).isEqualTo(firstExpectedQuestion.answers());
    }

    @DisplayName("Проверка количества верных ответов")
    @Test
    void numberCorrectAnswersTest() {
        var student = new Student("Ivanov", "Ivan");

        var inputAnswer = 1;
        when(localizedIOService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(inputAnswer);
        var result = testService.executeTestFor(student);
        assertThat(result.getRightAnswersCount()).isEqualTo(1);

        inputAnswer = 2;
        when(localizedIOService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(inputAnswer);
        var result2 = testService.executeTestFor(student);
        assertThat(result2.getRightAnswersCount()).isZero();
    }
}
