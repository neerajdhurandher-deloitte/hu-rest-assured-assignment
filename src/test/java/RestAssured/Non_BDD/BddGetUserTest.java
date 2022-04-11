package RestAssured.Non_BDD;

import RestAssured.BaseGetUser;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class BddGetUserTest extends BaseGetUser {

    Response response;
    ExtentTest extentTest;
    JSONArray jsonArray;

    @BeforeTest
    public void init(){
        extentTest = extent.createTest("Get User Test using BDD format ");
        extentTest.log(Status.PASS,"Test setup");
    }

    @Test
    public void getUsers(){
        requestSpecification = super.getRequestSpecBuilder().setBasePath(get_user_endpoint).build();

        responseSpecification = super.getResponseSpecBuilder().expectStatusCode(200).expectBody(JsonSchemaValidator.
                matchesJsonSchema(
                        new File("src/main/resources/JsonSchema/get_user_json_schema.json"))).build();

        response = given().spec(requestSpecification)
                .when()
                .get().then()
                .spec(responseSpecification).extract().response();

        jsonArray = new JSONArray(response.body().jsonPath().getList("data"));

    }

    @Test (priority = 2)
    public void genderValidation() {
        super.genderValidation(jsonArray,extentTest);
    }

    @Test (priority = 2)
    public void domainValidation() {
        super.domainValidation(jsonArray,extentTest);
    }

    @Test (priority = 2)
    public void idValidation() {
        super.idValidation(jsonArray,extentTest);
    }


}
