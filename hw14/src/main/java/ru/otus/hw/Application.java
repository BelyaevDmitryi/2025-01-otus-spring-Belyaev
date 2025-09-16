package ru.otus.hw;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.shell.command.annotation.CommandScan;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.relation.BookRepository;

@EnableMongoRepositories(basePackageClasses = MongoBookRepository.class)
@EnableJpaRepositories(basePackageClasses = BookRepository.class)
@CommandScan
@EnableMongock
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
