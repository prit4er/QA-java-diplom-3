package Chrome;

import PageObject.LoginPage;
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

public class RegisterTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Users/alekseykolobov/Downloads/chromedriver_mac64/chromedriver");
        driver = new ChromeDriver();
        long durationInMillis = Duration.ofSeconds(3000).toMillis();
        driver.manage().timeouts().implicitlyWait(durationInMillis, TimeUnit.MILLISECONDS);
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.open();
    }

    @Test
    @DisplayName("Проверка успешной регистрации")
    public void checkRegister() {
        Random random = new Random();
        String email = "testposhta" + random.nextInt(10000000) + "@yandex.ru";
        String password = "password" + random.nextInt(10000000);
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.inputName("Aleksey");
        registerPage.inputEmail(email);
        registerPage.inputPassword(password);
        registerPage.clickRegister();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitLoginPage();
        Assert.assertEquals(RegisterPage.PAGE_URL, driver.getCurrentUrl());
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

    @Test
    @DisplayName("Проверка ошибки при вводе некорректного пароля")
    public void checkRegisterWrongPassword() {
        Random random = new Random();
        String email = "testposhta" + random.nextInt(10000000) + "@yandex.ru";
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.inputName("Aleksey");
        registerPage.inputEmail(email);
        registerPage.inputPassword("!2qwe");
        registerPage.clickRegister();
        Assert.assertTrue(registerPage.loginErrorDisplayed());
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        dto.LoginUser loginUser = new dto.LoginUser(email, "!2qwe");
        Response response = dto.UserClient.postApiAuthLogin(loginUser);
        String responseString = response.body().asString();
        Gson gson = new Gson();
        dto.LoginUserResponse loginUserResponse = gson.fromJson(responseString, dto.LoginUserResponse.class);
        String accessToken = loginUserResponse.getAccessToken();
        if (accessToken != null) {
            dto.UserClient.deleteApiAuthUser(accessToken).then().assertThat().body("success", equalTo(true))
                          .and()
                          .body("message", equalTo("User successfully removed"))
                          .and()
                          .statusCode(202);
        }
    }

    @After
    public void cleanUp() {
        driver.quit();
    }
}