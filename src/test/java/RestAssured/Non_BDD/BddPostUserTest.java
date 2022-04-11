package RestAssured.Non_BDD;

import RestAssured.BasePostUser;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BddPostUserTest extends BasePostUser {
    Response response;
    ExtentTest extentTest;
    Map<String,String> post_data;

    @BeforeTest
    public void init(){
        extentTest = extent.createTest("Get User Test using BDD format ");
        extentTest.log(Status.PASS,"Test setup");
    }

    @Test
    public void post_user_data(){

        post_data = getUser();

        requestSpecification = super.getRequestSpecBuilder().setBasePath(post_user_endpoint).addHeader("Authorization",
                "Bearer " + authToken).setBody(post_data).build();

        responseSpecification = super.getResponseSpecBuilder().expectStatusCode(201).expectBody(JsonSchemaValidator.
                matchesJsonSchema(
                        new File("src/main/resources/JsonSchema/post_user_json_schema.json"))).build();

        response =   given().spec(requestSpecification)
                .when()
                .post().then()
                .spec(responseSpecification).extract().response();


        if(response.getStatusCode() == 201){
            super.validate_response(response, extentTest,post_data);
        }

        if(response.getStatusCode() == 422){

            System.out.println("bhai");

            readErrorMsg(response,extentTest);
        }
    }

}
