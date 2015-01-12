package net.thucydides.core.annotations.locators;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import net.thucydides.core.guice.Injectors;
import net.thucydides.core.pages.PageObject;
import net.thucydides.core.webdriver.Configuration;
import net.thucydides.core.webdriver.ElementLocatorFactorySelector;
import net.thucydides.core.webdriver.ElementProxyCreator;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

public class SmartElementProxyCreator implements ElementProxyCreator {

    @Override
    public void proxyElements(PageObject pageObject, WebDriver driver) {
//		ElementLocatorFactory finder = getElementLocatorFactorySelector().getLocatorFor(driver);
//        FieldDecorator decorator = new SmartFieldDecorator(finder, driver, pageObject);
//        PageFactory.initElements(decorator, pageObject);
        HtmlElementLoader.populatePageObject(pageObject, driver);
    }

    @Override
    public void proxyElements(PageObject pageObject, WebDriver driver, int timeoutInSeconds) {
//		ElementLocatorFactory finder = getElementLocatorFactorySelector().withTimeout(timeoutInSeconds).getLocatorFor(driver);
//        FieldDecorator decorator = new SmartFieldDecorator(finder, driver, pageObject);
//        PageFactory.initElements(decorator, pageObject);
        HtmlElementLoader.populatePageObject(pageObject, driver);

    }

    private ElementLocatorFactorySelector getElementLocatorFactorySelector() {
        Configuration configuration = Injectors.getInjector().getInstance(Configuration.class);
        return new ElementLocatorFactorySelector(configuration);
    }

}
