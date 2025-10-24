import io.restassured.response.Response;
import model.OrderModel;
import model.UserModel;
import org.junit.After;
import org.junit.Test;

import static data.TestData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CreateOrderTest.*;
import static steps.UserCreateStep.createUser;
import static steps.UserDeleteStep.deleteUser;

public class CreateOrderTest extends BaseAPITest {
    private UserModel newUser;
    private String accessToken;
    private Response response;
    private Response listOfIngredients;
    private OrderModel orderModel;
    private Response responseIngredients;

    @Test
    //можно создать заказ с авторизацией;
    public void createOrderTestSuccess(){

        newUser = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(newUser);
        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        responseIngredients = getIngredients();
        String[] listOfIngredients = new String[RANDOM_NUMBER];
        for (int i = 0; i < listOfIngredients.length; i++){
            listOfIngredients[i] = responseIngredients.jsonPath().getString("data[" + user.number().numberBetween(0,15) + "]._id");
        }
        orderModel = new OrderModel(listOfIngredients);
        createOrders(accessToken, orderModel)
                .then()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    //нельзя создать заказ без авторизации;
    public void createOrderTestWithoutAuth(){

        newUser = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(newUser);
        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        responseIngredients = getIngredients();
        String[] listOfIngredients = new String[RANDOM_NUMBER];
        for (int i = 0; i < listOfIngredients.length; i++){
            listOfIngredients[i] = responseIngredients.jsonPath().getString("data[" + user.number().numberBetween(0,15) + "]._id");
        }
        orderModel = new OrderModel(listOfIngredients);
        createOrdersWithoutAuth(orderModel)
                .then()
                .statusCode(HTTP_UNAUTHORIZED);
    }

    @Test
    //нельзя создать заказ без ингредиентов;
    public void createOrderTestWithoutIngredients(){

        newUser = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(newUser);
        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        String[] listOfIngredients = new String[0];
        orderModel = new OrderModel(listOfIngredients);
        createOrders(accessToken, orderModel)
                .then()
                .statusCode(HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    //нельзя создать заказ c неверным хэшем-ингредиентов;
    public void createOrderTestWithIncorrectIngredients(){

        newUser = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(newUser);
        accessToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        String[] listOfIngredients = new String[]{"1111", "2222", "3333"};
        orderModel = new OrderModel(listOfIngredients);
        createOrders(accessToken, orderModel)
                .then()
                .statusCode(HTTP_INTERNAL_ERROR);
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
