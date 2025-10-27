import io.restassured.RestAssured;
import org.junit.BeforeClass;
import static data.TestData.BASE_URI;

public class BaseAPITest {

    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI = BASE_URI;
    }
}
