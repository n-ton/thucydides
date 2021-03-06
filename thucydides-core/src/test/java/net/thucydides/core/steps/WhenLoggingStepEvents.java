package net.thucydides.core.steps;

import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestStepFactory;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.MockEnvironmentVariables;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.*;

public class WhenLoggingStepEvents {

    @Mock
    Logger logger;

    @Mock
    Logger quietLogger;

    @Mock
    StepFailure stepFailure;

    EnvironmentVariables environmentVariables;

    ConsoleLoggingListener consoleLoggingListener;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        environmentVariables = new MockEnvironmentVariables();
        consoleLoggingListener = new ConsoleLoggingListener(environmentVariables, logger);
    }

    class SomeTestClass {}

    @Test
    public void should_print_header_banner_before_tests() {

        consoleLoggingListener.testSuiteStarted(SomeTestClass.class);

        verify(logger).info(contains(
        "-------------------------------------------------------------------------------------------------------\n" +
        ".___________. __    __   __    __    ______ ____    ____  _______   __   _______   _______     _______.\n" +
        "|           ||  |  |  | |  |  |  |  /      |\\   \\  /   / |       \\ |  | |       \\ |   ____|   /       |\n" +
        "`---|  |----`|  |__|  | |  |  |  | |  ,----' \\   \\/   /  |  .--.  ||  | |  .--.  ||  |__     |   (----`\n" +
        "    |  |     |   __   | |  |  |  | |  |       \\_    _/   |  |  |  ||  | |  |  |  ||   __|     \\   \\    \n" +
        "    |  |     |  |  |  | |  `--'  | |  `----.    |  |     |  '--'  ||  | |  '--'  ||  |____.----)   |   \n" +
        "    |__|     |__|  |__|  \\______/   \\______|    |__|     |_______/ |__| |_______/ |_______|_______/    \n" +
        "                                                                                                       \n" +
        "-------------------------------------------------------------------------------------------------------\n"));

    }

    @Test
    public void should_print_shortened_header_banner_before_tests() {

        EnvironmentVariables environmentVariables = new MockEnvironmentVariables();
        environmentVariables.setProperty("thucydides.console.headings", "normal");
        ConsoleLoggingListener consoleLoggingListener = new ConsoleLoggingListener(environmentVariables, logger);
        verify(logger).info(contains("THUCYDIDES"));

    }

    @Test
    public void should_print_full_header_banner_before_tests() {

        EnvironmentVariables environmentVariables = new MockEnvironmentVariables();
        environmentVariables.setProperty("thucydides.console.headings", "ascii");
        Logger logger = mock(Logger.class);
        ConsoleLoggingListener consoleLoggingListener = new ConsoleLoggingListener(environmentVariables, logger);
        verify(logger).info(contains(
                "-------------------------------------------------------------------------------------------------------\n" +
                        ".___________. __    __   __    __    ______ ____    ____  _______   __   _______   _______     _______.\n" +
                        "|           ||  |  |  | |  |  |  |  /      |\\   \\  /   / |       \\ |  | |       \\ |   ____|   /       |\n" +
                        "`---|  |----`|  |__|  | |  |  |  | |  ,----' \\   \\/   /  |  .--.  ||  | |  .--.  ||  |__     |   (----`\n" +
                        "    |  |     |   __   | |  |  |  | |  |       \\_    _/   |  |  |  ||  | |  |  |  ||   __|     \\   \\    \n" +
                        "    |  |     |  |  |  | |  `--'  | |  `----.    |  |     |  '--'  ||  | |  '--'  ||  |____.----)   |   \n" +
                        "    |__|     |__|  |__|  \\______/   \\______|    |__|     |_______/ |__| |_______/ |_______|_______/    \n" +
                        "                                                                                                       \n" +
                        "-------------------------------------------------------------------------------------------------------\n"));

    }

    @Test
    public void should_not_print_header_banner_before_tests_in_quiet_mode() {

        environmentVariables.setProperty("thucydides.logging", "QUIET");
        ConsoleLoggingListener quietListener = new ConsoleLoggingListener(environmentVariables, quietLogger);
        quietListener.testSuiteStarted(SomeTestClass.class);

        verify(quietLogger, never()).info(contains(
                "-------------------------------------------------------------------------------------------------------\n" +
                        ".___________. __    __   __    __    ______ ____    ____  _______   __   _______   _______     _______.\n" +
                        "|           ||  |  |  | |  |  |  |  /      |\\   \\  /   / |       \\ |  | |       \\ |   ____|   /       |\n" +
                        "`---|  |----`|  |__|  | |  |  |  | |  ,----' \\   \\/   /  |  .--.  ||  | |  .--.  ||  |__     |   (----`\n" +
                        "    |  |     |   __   | |  |  |  | |  |       \\_    _/   |  |  |  ||  | |  |  |  ||   __|     \\   \\    \n" +
                        "    |  |     |  |  |  | |  `--'  | |  `----.    |  |     |  '--'  ||  | |  '--'  ||  |____.----)   |   \n" +
                        "    |__|     |__|  |__|  \\______/   \\______|    |__|     |_______/ |__| |_______/ |_______|_______/    \n" +
                        "                                                                                                       \n" +
                        "-------------------------------------------------------------------------------------------------------\n"));

    }

    @Test
    public void should_log_test_suite_name_when_a_test_suite_starts() {
        consoleLoggingListener.testSuiteStarted(SomeTestClass.class);

        verify(logger).info(contains("Test Suite Started: Some test class"));
    }

    @Test
    public void should_not_log_test_suite_name_when_a_test_suite_starts_in_quiet_mode() {
        environmentVariables.setProperty("thucydides.logging","QUIET");

        consoleLoggingListener.testSuiteStarted(SomeTestClass.class);

        verify(logger, never()).info(contains("Test Suite Started: Some test class"));
    }

    @Test
    public void should_log_test_banner_when_test_starts() {
        consoleLoggingListener.testStarted("Some test");

        verify(logger).info(contains(
              "\n  _____ _____ ____ _____   ____ _____  _    ____ _____ _____ ____  \n" +
                " |_   _| ____/ ___|_   _| / ___|_   _|/ \\  |  _ \\_   _| ____|  _ \\ \n" +
                "   | | |  _| \\___ \\ | |   \\___ \\ | | / _ \\ | |_) || | |  _| | | | |\n" +
                "   | | | |___ ___) || |    ___) || |/ ___ \\|  _ < | | | |___| |_| |\n" +
                "   |_| |_____|____/ |_|   |____/ |_/_/   \\_\\_| \\_\\|_| |_____|____/ \n"));
    }

    @Test
    public void should_log_test_name_when_test_starts() {
        consoleLoggingListener.testStarted("Some test");

        verify(logger).info(contains("TEST STARTED: Some test"));
    }

    @Test
    public void should_log_message_when_test_fails() {
        TestOutcome testOutcome = failingTestOutcome();

        consoleLoggingListener.testFinished(testOutcome);

        verify(logger).info(contains("TEST FAILED: Some test"));
    }

    @Test
    public void should_not_log_messages_when_test_fails_in_quiet_mode() {
        environmentVariables.setProperty("thucydides.logging","QUIET");
        TestOutcome testOutcome = failingTestOutcome();

        consoleLoggingListener.testFinished(testOutcome);

        verify(logger,never()).info(contains("TEST FAILED: Some test"));
    }

    @Test
    public void should_log_message_when_test_is_pending() {
        TestOutcome testOutcome = pendingTestOutcome();

        consoleLoggingListener.testFinished(testOutcome);

        verify(logger).info(contains("TEST PENDING: Some test"));
    }

    @Test
    public void should_not_log_messages_when_test_is_pending_in_quiet_mode() {
        environmentVariables.setProperty("thucydides.logging","QUIET");
        TestOutcome testOutcome = pendingTestOutcome();

        consoleLoggingListener.testFinished(testOutcome);

        verify(logger,never()).info(contains("TEST PENDING: Some test"));
    }

    @Test
    public void should_log_message_when_test_is_skipped() {
        TestOutcome testOutcome = skippedTestOutcome();

        consoleLoggingListener.testFinished(testOutcome);

        verify(logger).info(contains("TEST SKIPPED: Some test"));
    }

    @Test
    public void should_not_log_messages_when_test_is_skipped_in_quiet_mode() {
        environmentVariables.setProperty("thucydides.logging","QUIET");
        TestOutcome testOutcome = skippedTestOutcome();

        consoleLoggingListener.testFinished(testOutcome);

        verify(logger,never()).info(contains("TEST SKIPPED: Some test"));
    }

//    @Test
//    public void should_log_message_when_test_fails_directly() {
//        consoleLoggingListener.testFailed(null, new AssertionError("something broke"));
//
//        verify(logger).info(contains("something broke"));
//    }

    @Test
    public void should_not_log_messages_when_test_fails_directly_in_quiet_mode() {
        environmentVariables.setProperty("thucydides.logging","QUIET");

        consoleLoggingListener.testFailed(null, new AssertionError("something broke"));

        verify(logger,never()).info(contains("something broke"));
    }
    @Test
    public void should_log_message_when_test_is_ignored() {
        consoleLoggingListener.testIgnored();

        verify(logger).info(contains("TEST IGNORED"));
    }

    @Test
    public void should_not_log_messages_when_test_is_ignored_in_quiet_mode() {
        environmentVariables.setProperty("thucydides.logging","QUIET");

        consoleLoggingListener.testIgnored();

        verify(logger,never()).info(contains("TEST IGNORED"));
    }

    @Test
    public void should_log_message_when_test_passes() {
        TestOutcome testOutcome = successfulTestOutcome();

        consoleLoggingListener.testFinished(testOutcome);

        verify(logger).info(contains("TEST PASSED: Some test"));
    }

    @Test
    public void should_not_log_messages_when_test_passes_in_quiet_mode() {
        environmentVariables.setProperty("thucydides.logging","QUIET");
        TestOutcome testOutcome = successfulTestOutcome();

        consoleLoggingListener.testFinished(testOutcome);

        verify(logger,never()).info(contains("TEST PASSED: Some test"));
    }    
    
    @Test
    public void should_log_test_suite_name_when_a_test_story_starts() {
        consoleLoggingListener.testSuiteStarted(Story.from(SomeTestClass.class));

        verify(logger).info(contains("Test Suite Started: Some test class"));
    }

    @Test
    public void should_log_test_name_when_a_test_starts() {
        consoleLoggingListener.testStarted("some_test");

        verify(logger).info(contains("TEST STARTED: some_test"));
    }

    @Test
    public void should_log_step_name_when_a_step_starts_if_in_verbose_mode() {
        environmentVariables.setProperty("thucydides.logging","VERBOSE");
        consoleLoggingListener.stepStarted(ExecutedStepDescription.withTitle("some step"));

        verify(logger).info(contains("STARTING STEP some step"));
    }

    @Test
    public void should_log_step_name_when_a_skipped_step_starts_if_in_verbose_mode() {
        environmentVariables.setProperty("thucydides.logging","VERBOSE");
        consoleLoggingListener.skippedStepStarted(ExecutedStepDescription.withTitle("some step"));

        verify(logger).info(contains("STARTING STEP some step"));
    }

    @Test
    public void should_log_step_finish_events_if_in_verbose_mode() {
        environmentVariables.setProperty("thucydides.logging", "VERBOSE");
        consoleLoggingListener.stepFinished();

        verify(logger).info(contains("FINISHING STEP"));
    }

    @Test
    public void should_log_step_failure_if_in_verbose_mode() {
        environmentVariables.setProperty("thucydides.logging", "VERBOSE");
        when(stepFailure.getMessage()).thenReturn("for some reason");

        consoleLoggingListener.stepFailed(stepFailure);

        verify(logger).info(contains("STEP FAILED: for some reason"));
    }

    @Test
    public void should_log_skipped_step_if_in_verbose_mode() {
        environmentVariables.setProperty("thucydides.logging", "VERBOSE");

        consoleLoggingListener.stepIgnored();

        verify(logger).info(contains("IGNORING STEP"));
    }

    @Test
    public void should_log_pending_step_if_in_verbose_mode() {
        environmentVariables.setProperty("thucydides.logging", "VERBOSE");

        consoleLoggingListener.stepPending();

        verify(logger).info(contains("PENDING STEP"));
    }

    @Test
    public void should_log_pending_step_with_message_if_in_verbose_mode() {
        environmentVariables.setProperty("thucydides.logging", "VERBOSE");

        consoleLoggingListener.stepPending("for some reason");

        verify(logger).info(contains("PENDING STEP (for some reason)"));
    }

    @Test
    public void should_log_no_step_events_if_not_in_verbose_mode() {
        consoleLoggingListener.stepStarted(ExecutedStepDescription.withTitle("some step"));
        consoleLoggingListener.skippedStepStarted(ExecutedStepDescription.withTitle("some skipped step"));
        consoleLoggingListener.stepFinished();
        consoleLoggingListener.stepFailed(stepFailure);
        consoleLoggingListener.stepIgnored();
        consoleLoggingListener.stepPending();
        consoleLoggingListener.stepPending("for some reason");
        consoleLoggingListener.stepPending("for some reason");

        verify(logger,never()).info(contains("STEP"));
    }

    @Test
    public void should_not_log_test_name_when_a_test_starts_if_logging_disabled() {
        environmentVariables.setProperty("thucydides.logging","QUIET");

        consoleLoggingListener.testStarted("some_test");

        verify(logger, never()).info(contains("TEST: some_test"));
    }


    private TestOutcome pendingTestOutcome() {
        TestOutcome testOutcome = TestOutcome.forTest("some_test", SomeTestClass.class);
        testOutcome.recordStep(TestStepFactory.forAPendingTestStepCalled("do_something"));
        return testOutcome;
    }

    private TestOutcome skippedTestOutcome() {
        TestOutcome testOutcome = TestOutcome.forTest("some_test", SomeTestClass.class);
        testOutcome.recordStep(TestStepFactory.forASkippedTestStepCalled("do_something"));
        return testOutcome;
    }

    private TestOutcome successfulTestOutcome() {
        TestOutcome testOutcome = TestOutcome.forTest("some_test", SomeTestClass.class);
        testOutcome.recordStep(TestStepFactory.forASuccessfulTestStepCalled("do_something"));
        return testOutcome;
    }

    private TestOutcome failingTestOutcome() {
        TestOutcome testOutcome = TestOutcome.forTest("some_test", SomeTestClass.class);
        testOutcome.recordStep(TestStepFactory.forABrokenTestStepCalled("do_something", new AssertionError("something broke")));
        return testOutcome;
    }

}
