package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestServiceImplTest {
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new ClassPathXmlApplicationContext("/spring-context.xml");
    }

    @Test
    void testServiceNotEmptyTest() {
        TestService testService = context.getBean(TestServiceImpl.class);
        assertNotNull(testService);
    }
}
