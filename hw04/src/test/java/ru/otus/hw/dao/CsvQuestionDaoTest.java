package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.config.AppProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CsvQuestionDaoTest {

    private QuestionDao dao;

    @BeforeEach
    void setUp() {
        AppProperties appProperties = Mockito.mock(AppProperties.class);
        Mockito.when(appProperties.getTestFileName()).thenReturn("questions.csv");
        dao = new CsvQuestionDao(appProperties);
    }

    @Test
    void csvQuestionDaoTest() {
        assertNotNull(dao);
        assertEquals(3, dao.findAll().size());
    }
}
