package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.AppProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @MockitoBean
    private AppProperties appProperties;

    @Autowired
    private CsvQuestionDao dao;

    @Test
    void csvQuestionDaoTest() {
        when(appProperties.getTestFileName()).thenReturn("questions.csv");
        assertNotNull(dao);
        assertEquals(3, dao.findAll().size());
    }
}
