package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.LoginModel;

import static io.restassured.RestAssured.given;

public class UserLoginStep {

    public static final String PATH_LOGIN = "/api/auth/login";

    @Step("Send POST request to api/auth/login - login courier")
    public static Response loginUser(String accessToken, LoginModel loginModel){
        return given()
                .log().all()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(loginModel)
                .when()
                .post(PATH_LOGIN)
                .then()
                .log().all()
                .extract().response();
    }
}
