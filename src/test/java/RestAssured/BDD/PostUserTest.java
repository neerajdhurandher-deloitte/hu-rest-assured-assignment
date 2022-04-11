package RestAssured.BDD;

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

public class PostUserTest extends BasePostUser {

    Response response;
    ExtentTest extentTest;
    Map<String,String> post_data;

    @BeforeTest
    public void init(){
        extentTest = extent.createTest("Post User Test");
        extentTest.log(Status.PASS,"Test setup");
    }

    @Test
    public void postUser(){

        post_data = getUser();

        log.info("post user details end point hit");

        // TODO debug
//        post_data = new HashMap<>();
//
//        post_data.put("name","hfa");
//        post_data.put("email","kah_gjdh@gmail.com");
//        post_data.put("gender","male");
//        post_data.put("status","active");

        response = given().
                baseUri(base_url).
                header("Content-Type","application/json").
                header("Authorization","Bearer " + authToken).
                body(post_data).
                when().
                post(post_user_endpoint).
                then().assertThat().body(JsonSchemaValidator.
                        matchesJsonSchema(
                                new File("src/main/resources/JsonSchema/post_user_json_schema.json")))
                .statusCode(201).contentType("application/json; charset=utf-8").extract().response();

        log.info(response.body().print());

        if(response.getStatusCode() == 201){
            super.validate_response(response, extentTest,post_data);
        }

        if(response.getStatusCode() == 422){

            System.out.println("bhai");

            readErrorMsg(response,extentTest);
        }


    }


}
