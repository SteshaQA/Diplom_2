import io.restassured.response.Response;
import model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.UserCreateStep.createUser;

@RunWith(Parameterized.class)
public class ParametrisedCreateUser extends BaseAPITest {
    private final String email;
    private final String password;
    private final String name;
    private UserModel newUser;

    public ParametrisedCreateUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[] getInputData() {
        return new Object[][]{
                {EMAIL, PASSWORD, null},
                {EMAIL, null, NAME},
                {null, PASSWORD, NAME},
        };
    }

    @Test
    public void createUserWithoutRequiredField(){

        newUser = new UserModel(email, password, name);
        createUser(newUser)
                .then()
                .statusCode(HTTP_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }
}

