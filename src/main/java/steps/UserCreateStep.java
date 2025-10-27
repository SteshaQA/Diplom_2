package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import model.UserModel;

public class UserCreateStep {

    public static final String PATH_CREATE = "/api/auth/register";

    @Step("Send POST request to api/auth/register - create courier")
    public static Response createUser(UserModel userModel){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(userModel)
                .when()
                .post(PATH_CREATE)
                .then()
                .log().all()
                .extract().response();
    }
}
