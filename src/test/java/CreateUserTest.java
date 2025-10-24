import io.restassured.response.Response;
import model.UserModel;
import org.junit.After;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.UserCreateStep.createUser;
import static steps.UserDeleteStep.deleteUser;

public class CreateUserTest extends BaseAPITest{
    private UserModel newUser;
    private String accessToken;
    private Response response;

    @Test
    //можно создать уникального пользователя;
    public void createUserTestSuccess(){

        newUser = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(newUser);
        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        response.then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    //можно создать пользователя, который уже зарегистрирован;
    public void createTwoIdenticalUserTestSuccess(){

        newUser = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(newUser);
        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        Response response2 = createUser(newUser);
        response2.then()
                .statusCode(HTTP_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }



    @After
    public void deleteUserTestSuccess(){
        try {
            if (response != null) {
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
