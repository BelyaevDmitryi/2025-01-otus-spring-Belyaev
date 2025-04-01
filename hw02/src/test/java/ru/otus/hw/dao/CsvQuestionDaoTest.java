package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CsvQuestionDaoTest {
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
         context = new ClassPathXmlApplicationContext("/spring-context.xml");
    }

    @Test
    void testCsvQuestionDao() throws IOException {
        QuestionDao questionDao = context.getBean(CsvQuestionDao.class);
        assertNotNull(questionDao);
        assertEquals(3, questionDao.findAll().size());
    }
}
