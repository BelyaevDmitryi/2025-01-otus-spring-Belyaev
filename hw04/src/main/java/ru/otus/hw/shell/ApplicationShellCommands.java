package ru.otus.hw.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Application commands test student's")
public class ApplicationShellCommands {

    private final TestRunnerService testRunnerService;

    @Autowired
    public ApplicationShellCommands(TestRunnerService testRunnerService) {
        this.testRunnerService = testRunnerService;
    }

    @ShellMethod(value = "Start command", key = {"r", "run"})
    public void run() {
        testRunnerService.run();
    }
}
