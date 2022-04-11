package RestAssured.BDD;

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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetUsersTest extends BaseGetUser {

    Response response;
    ExtentTest extentTest;
    JSONArray jsonArray;

    @BeforeTest
    public void init(){
        extentTest = extent.createTest("Get User Test");
        extentTest.log(Status.PASS,"Test setup");
    }

    @Test
    public void getUsers(){

        log.info("get user details end point hit");

        response = given().
                baseUri(base_url).
                header("Content-Type","application/json").
                when().
                get(get_user_endpoint).
                then().assertThat().body(JsonSchemaValidator.
                        matchesJsonSchema(
                                new File("src/main/resources/JsonSchema/get_user_json_schema.json")))
                .statusCode(200).contentType("application/json; charset=utf-8").extract().response();

        jsonArray = new JSONArray(response.body().jsonPath().getList("data"));


        log.info(response.body().print());


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
