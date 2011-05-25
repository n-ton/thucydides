package net.thucydides.junit.runners;

import net.thucydides.core.steps.PagesAnnotatedField;
import net.thucydides.core.steps.StepsAnnotatedField;
import net.thucydides.junit.internals.ManagedWebDriverAnnotatedField;
import org.openqa.selenium.WebDriver;

/**
 * Utility class used to inject fields into a test case.
 * @author johnsmart
 *
 */
public final class TestCaseAnnotations {

    private final Object testCase;

    private TestCaseAnnotations(Object testCase) {
        this.testCase = testCase;
    }

    public static TestCaseAnnotations forTestCase(Object testCase) {
        return new TestCaseAnnotations(testCase);
    }

    public static void checkThatTestCaseIsCorrectlyAnnotated(Class<?> testClass) {
        checkThatManagedFieldIsDefined(testClass);
        checkThatStepsFieldIsDefined(testClass);
        checkThatPagesFieldIsDefined(testClass);
    }
    /**
     * There must be a WebDriver field in the test case annotated with the Managed annotation.
     */
    private static void checkThatManagedFieldIsDefined(Class<?> testClass) {
        ManagedWebDriverAnnotatedField.findFirstAnnotatedField(testClass);
    }

    /**
     * There must be a ScenarioSteps field in the test case annotated with the Steps annotation.
     */
    private static void checkThatStepsFieldIsDefined(Class<?> testClass) {
        StepsAnnotatedField.findMandatoryAnnotatedFields(testClass);
    }

    /**
     * There must be a Pages field in the test case annotated with the ManagedPages annotation.
     */
    private static void checkThatPagesFieldIsDefined(Class<?> testClass) {
        PagesAnnotatedField.findFirstAnnotatedField(testClass);
    }

    
    /**
     * Instantiate the @Managed-annotated WebDriver instance with current WebDriver.
     */
    public void injectDriver(final WebDriver driver) {
        ManagedWebDriverAnnotatedField webDriverField = ManagedWebDriverAnnotatedField
                .findFirstAnnotatedField(testCase.getClass());

        webDriverField.setValue(testCase, driver);
    }

}
