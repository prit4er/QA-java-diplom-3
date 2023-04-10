package dto;

import com.google.gson.Gson;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserClient {
    private final static String API_AUTH_REGISTER = "/api/auth/register";
    private final static String API_AUTH_LOGIN = "/api/auth/login";
    private final static String API_AUTH_USER = "/api/auth/user";

    public UserClient() {
    }

    public static Response postApiAuthRegister(CreateUser createUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .post(API_AUTH_REGISTER);
    }

    public static Response postApiAuthLogin(LoginUser loginUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(API_AUTH_LOGIN);
    }

    public static String getTokenFromApiAuthLogin(LoginUser loginUser) {
        Response response = dto.UserClient.postApiAuthLogin(loginUser);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        String responseString = response.body().asString();
        Gson gson = new Gson();
        dto.LoginUserResponse loginUserResponse = gson.fromJson(responseString, dto.LoginUserResponse.class);
        return loginUserResponse.getAccessToken();
    }

    public static Response deleteApiAuthUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .delete(API_AUTH_USER);
    }

    public static Response getApiAuthUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .get(API_AUTH_USER);
    }

    public static Response getApiAuthUser() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(API_AUTH_USER);
    }

    public static Response patchApiAuthUser(String accessToken, CreateUser createUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .and()
                .body(createUser)
                .when()
                .patch(API_AUTH_USER);
    }

    public static Response patchApiAuthUser(CreateUser createUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .patch(API_AUTH_USER);
    }

}
