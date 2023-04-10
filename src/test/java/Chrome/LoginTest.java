package Chrome;
import PageObject.LoginPage;
import PageObject.MainPage;
import PageObject.PasswordPage;
import PageObject.RegisterPage;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;

public class LoginTest {

    private WebDriver driver;
    private String email;
    private String password;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        System.setProperty("webdriver.chrome.driver", "/Users/alekseykolobov/Downloads/chromedriver_mac64/chromedriver");
        driver = new ChromeDriver();
        long durationInMillis = Duration.ofSeconds(3000).toMillis();
        driver.manage().timeouts().implicitlyWait(durationInMillis, TimeUnit.MILLISECONDS);
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.open();
        Random random = new Random();
        this.email = "testposhta" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "password" + random.nextInt(10000000);
        registerPage.inputName("Aleksey");
        registerPage.inputEmail(email);
        registerPage.inputPassword(password);
        registerPage.clickRegister();
    }

    @Test
    @DisplayName("Проверка входа по кнопке «Войти в аккаунт» на главной")
    public void checkLoginLkButton() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.clickLoginLkButton();
        Assert.assertEquals(LoginPage.PAGE_URL, driver.getCurrentUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLogin();
        mainPage.waitMainPage();
        Assert.assertEquals(MainPage.PAGE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Проверка входа через кнопку «Личный кабинет»")
    public void checkLoginLkLink() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.clickLoginLkLink();
        Assert.assertEquals(LoginPage.PAGE_URL, driver.getCurrentUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLogin();
        mainPage.waitMainPage();
        Assert.assertEquals(MainPage.PAGE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Проверка входа через кнопку в форме регистрации")
    public void checkLoginOnRegisterPage() {
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.open();
        registerPage.clickLogin();
        Assert.assertEquals(LoginPage.PAGE_URL, driver.getCurrentUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLogin();
        MainPage mainPage = new MainPage(driver);
        mainPage.waitMainPage();
        Assert.assertEquals(MainPage.PAGE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Проверка входа через кнопку в форме восстановления пароля")
    public void checkLoginOnForgotPasswordButton() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.clickForgotPassword();
        Assert.assertEquals(PasswordPage.PAGE_URL, driver.getCurrentUrl());
        PasswordPage forgotPasswordPage = new PasswordPage(driver);
        forgotPasswordPage.clickLogin();
        Assert.assertEquals(LoginPage.PAGE_URL, driver.getCurrentUrl());
        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLogin();
        MainPage mainPage = new MainPage(driver);
        mainPage.waitMainPage();
        Assert.assertEquals(MainPage.PAGE_URL, driver.getCurrentUrl());
    }

    @After
    public void cleanUp() {
        driver.quit();
        dto.LoginUser loginUser = new dto.LoginUser(email, password);
        String accessToken = dto.UserClient.getTokenFromApiAuthLogin(loginUser);
        dto.UserClient.deleteApiAuthUser(accessToken).then().assertThat().body("success", equalTo(true))
                      .and()
                      .body("message", equalTo("User successfully removed"))
                      .and()
                      .statusCode(202);
    }
}