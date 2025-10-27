package data;

import com.github.javafaker.Faker;

public class TestData {

    public static final String BASE_URI = "https://stellarburgers.education-services.ru/";

    public static Faker user = new Faker();
    public static final String EMAIL = user.internet().emailAddress();
    public static final String PASSWORD = user.regexify("[a-zA-Z0-9]{4}");
    public static final String NAME = user.name().firstName() + System.currentTimeMillis();
    public static final int RANDOM_NUMBER = user.number().numberBetween(1,16);
}
