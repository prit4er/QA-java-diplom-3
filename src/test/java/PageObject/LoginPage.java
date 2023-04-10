package PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    public static final String PAGE_URL = "https://stellarburgers.nomoreparties.site/login";
    //локатор кнопки Зарегистрироваться
    public final By registerButton = By.xpath(".//a[text()='Зарегистрироваться']");
    //локатор поля Email
    private final By emailField = By.xpath(".//input[@type='text']");
    //локатор поля Пароль
    private final By passwordField = By.xpath(".//input[@type='password']");
    //локатор кнопки Восстановить пароль
    private final By forgotPasswordButton = By.xpath(".//a[text()='Восстановить пароль']");
    //локатор кнопки Войти
    private final By loginButton = By.xpath(".//button[text()='Войти']");
    private final WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    //метод заполнения поля ввода email
    public void inputEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }
    //метод заполнения поля ввода password
    public void inputPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }
    //метод клика по кнопке Войти
    public void clickLogin() {
        driver.findElement(loginButton).click();
    }
    //метод клика по кнопке Восстановить пароль
    public void clickForgotPassword() {
        driver.findElement(forgotPasswordButton).click();
    }

    //метод клика по кнопке Зарегистрироваться
    public void clickRegister() {
        driver.findElement(registerButton).click();
    }

    public void open() {
        driver.get(PAGE_URL);
    }

    public void waitLoginPage() {
        new WebDriverWait(driver, 3)
                .until(ExpectedConditions.elementToBeClickable(emailField));
    }
}