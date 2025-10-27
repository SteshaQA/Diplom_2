package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserDeleteStep {

    public static final String PATH_DELETE = "/api/auth/user";

    @Step("Send DELETE request to api/auth/user - delete courier")
    public static Response deleteUser(String accessToken){

        return  given()
                .log().all()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .when()
                .delete(PATH_DELETE)
                .then()
                .log().all()
                .extract().response();
    }
}
