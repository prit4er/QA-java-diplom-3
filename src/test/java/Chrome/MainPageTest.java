package Chrome;

import PageObject.MainPage;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MainPageTest {
    private WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Users/alekseykolobov/Downloads/chromedriver_mac64/chromedriver");
    }

    @Test
    @DisplayName("Проверка перехода по разделам конструктора")
    public void checkLoginLkButton() {
        driver = new ChromeDriver();
        long durationInMillis = Duration.ofSeconds(3000).toMillis();
        driver.manage().timeouts().implicitlyWait(durationInMillis, TimeUnit.MILLISECONDS);
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        Assert.assertEquals("Булки", mainPage.checkCurrentTabHeaderText());
        mainPage.clickSouseButton();
        mainPage.waitNachinkiHeader();
        Assert.assertEquals("Соусы", mainPage.checkCurrentTabHeaderText());
        mainPage.clickNachinkiButton();
        mainPage.waitBulkiHeader();
        Assert.assertEquals("Начинки", mainPage.checkCurrentTabHeaderText());
        mainPage.clickBulkiButton();
    }

    @After
    public void cleanUp() {
        driver.quit();
    }
}