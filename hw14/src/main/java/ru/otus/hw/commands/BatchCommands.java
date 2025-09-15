package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent(value = "batch command")
public class BatchCommands {

    private final Logger logger = LoggerFactory.getLogger(BatchCommands.class);


    private final Job migrateJob;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "start migration job", key = "mi")
    public void startMigrationJob() throws Exception {
        JobExecution execution = jobLauncher.run(migrateJob, new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters()
        );
        logger.info(execution.toString());
    }
}
