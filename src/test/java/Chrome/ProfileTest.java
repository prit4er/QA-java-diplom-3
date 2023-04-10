package Chrome;
import PageObject.LoginPage;
import PageObject.MainPage;
import PageObject.ProfilePage;
import PageObject.RegisterPage;
import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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

public class ProfileTest {

    private WebDriver driver;
    private String email;
    private String password;

    @Before
    public void setUp() {
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
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.clickLoginLkButton();
        Assert.assertEquals(LoginPage.PAGE_URL, driver.getCurrentUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLogin();
    }

    @Test
    @DisplayName("Проверка перехода на главную страницу по клику на логотип Stellar Burgers")
    public void checkAccountButton() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickLoginLkLink();
        ProfilePage accountProfilePage = new ProfilePage(driver);
        accountProfilePage.waitAccountProfilePage();
        Assert.assertEquals(ProfilePage.PAGE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Проверка перехода на главную страницу по клику на «Конструктор»")
    public void checkConstructorButton() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickLoginLkLink();
        ProfilePage accountProfilePage = new ProfilePage(driver);
        accountProfilePage.clickConstructorButton();
        mainPage.waitMainPage();
        Assert.assertEquals(MainPage.PAGE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Проверка выхода по кнопке «Выйти» в личном кабинете")
    public void checkExitButton() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickLoginLkLink();
        ProfilePage accountProfilePage = new ProfilePage(driver);
        accountProfilePage.clickExitButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitLoginPage();
        Assert.assertEquals(LoginPage.PAGE_URL, driver.getCurrentUrl());
    }

    @After
    public void cleanUp() {
        driver.quit();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        dto.LoginUser loginUser = new dto.LoginUser(email, password);
        Response response = dto.UserClient.postApiAuthLogin(loginUser);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        String responseString = response.body().asString();
        Gson gson = new Gson();
        dto.LoginUserResponse loginUserResponse = gson.fromJson(responseString, dto.LoginUserResponse.class);
        String accessToken = loginUserResponse.getAccessToken();
        dto.UserClient.deleteApiAuthUser(accessToken).then().assertThat().body("success", equalTo(true))
                      .and()
                      .body("message", equalTo("User successfully removed"))
                      .and()
                      .statusCode(202);
    }
}
