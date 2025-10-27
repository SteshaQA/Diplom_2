import io.restassured.response.Response;
import model.LoginModel;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static steps.UserCreateStep.createUser;
import static steps.UserDeleteStep.deleteUser;
import static steps.UserLoginStep.loginUser;

public class LoginUserTest extends BaseAPITest {
    private UserModel newUser;
    private String accessToken;
    private Response response;
    private LoginModel loginModel;
    private Response responseLogin;
    private String accessTokenLogin;

    @Before
    public void createUserBeforeTests() {
        newUser = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(newUser);
    }

    @Test
    //можно осуществить вход под существующим пользователем;
    public void loginUserTestSuccess() {

        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        String email = newUser.getEmail();
        String password = newUser.getPassword();
        loginModel = new LoginModel(email, password);
        responseLogin = loginUser(accessToken, loginModel);
        accessTokenLogin = responseLogin.jsonPath().getString("accessToken").replace("Bearer ", "");
        responseLogin.then()
                     .statusCode(HTTP_OK)
                     .body("success", equalTo(true))
                     .body("user", notNullValue());
    }

    @Test
    //нельзя осуществить вход с неверным логином;
    public void loginUserTestWithIncorrectLogin() {

        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        String email = newUser.getEmail();
        String password = newUser.getPassword();
        loginModel = new LoginModel(email + "ru", password);
        responseLogin = loginUser(accessToken, loginModel);
        if (responseLogin.getStatusCode() == HTTP_OK) {
            accessTokenLogin = responseLogin.jsonPath().getString("accessToken").replace("Bearer ", "");
        }
        responseLogin.then()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    //нельзя осуществить вход с неверным паролем;
    public void loginUserTestWithIncorrectPassword() {

        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        String email = newUser.getEmail();
        String password = newUser.getPassword();
        loginModel = new LoginModel(email, password + "aaa");
        responseLogin = loginUser(accessToken, loginModel);
        if (responseLogin.getStatusCode() == HTTP_OK) {
            accessTokenLogin = responseLogin.jsonPath().getString("accessToken").replace("Bearer ", "");
        }
        responseLogin.then()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }


    @After
    public void deleteUserTestSuccess(){
        try {
            if (responseLogin.getStatusCode() == HTTP_OK) {
                deleteUser(accessTokenLogin)
                        .then()
                        .statusCode(HTTP_ACCEPTED)
                        .body("success", equalTo(true));
            } else {
                deleteUser(accessToken)
                        .then()
                        .statusCode(HTTP_ACCEPTED)
                        .body("success", equalTo(true));
            }
        } catch (Exception e) {
            System.out.println("Пользователь не был создан");
        }
    }
}