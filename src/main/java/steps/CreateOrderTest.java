package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.OrderModel;

import static io.restassured.RestAssured.given;

public class CreateOrderTest {
    public static final String PATH_CREATE_ORDERS = "/api/orders";
    public static final String PATH_GET_INGREDIENTS = "/api/ingredients";

    @Step("Send GET request to /api/ingredients - get /api/ingredients")
    public static Response getIngredients(){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(PATH_GET_INGREDIENTS)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Send POST request to /api/orders - create order with auth")
    public static Response createOrders(String accessToken, OrderModel orderModel){
        return given()
                .log().all()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(orderModel)
                .when()
                .post(PATH_CREATE_ORDERS)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Send POST request to /api/orders - create order without auth")
    public static Response createOrdersWithoutAuth(OrderModel orderModel){
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(orderModel)
                .when()
                .post(PATH_CREATE_ORDERS)
                .then()
                .log().all()
                .extract().response();
    }

}
