package PageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PasswordPage {
    public static final String PAGE_URL = "https://stellarburgers.nomoreparties.site/forgot-password";
    private final By loginLink = By.xpath(".//a[text()='Войти']");
    private final WebDriver driver;

    public PasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickLogin() {
        driver.findElement(loginLink).click();
    }
}