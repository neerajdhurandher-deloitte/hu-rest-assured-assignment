package RestAssured.NonBDD;

import RestAssured.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PostUserTest extends BaseTest {

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
                                new File("src/main/java/JsonSchema/post_user_json_schema.json")))
                .statusCode(201).contentType("application/json; charset=utf-8").extract().response();

        log.info(response.body().print());

        if(response.getStatusCode() == 201){
            validate_response();
        }

        if(response.getStatusCode() == 422){

            System.out.println("bhai");

            readErrorMsg();
        }


    }

    private void validate_response() {

        boolean name_validation = response.body().jsonPath().getString("data.name").equals(post_data.get("name"));
        boolean email_validation = response.body().jsonPath().getString("data.email").equals(post_data.get("email"));
        boolean gender_validation = response.body().jsonPath().getString("data.gender").equals(post_data.get("gender"));
        boolean status_validation = response.body().jsonPath().getString("data.status").equals(post_data.get("status"));

        if(name_validation && email_validation && gender_validation && status_validation){
            log.info("Response is valid");
            extentTest.log(Status.PASS,"Response is valid");
        }else{
            log.info("Invalid Response");
            extentTest.log(Status.PASS,"Invalid Response");
        }

    }

    public void readErrorMsg(){

//          Actual: {"meta":null,"data":[{"field":"email","message":"has already been taken"}]}

        String error_message = response.body().jsonPath().getString("data.message");
        String error_field = response.body().jsonPath().getString("data.field");

        String error_str = "Error Message :- "+ error_field+ " " +error_message;

        log.error(response.body().toString());
        log.error(error_str);
        extentTest.log(Status.FAIL,response.body().toString());
        extentTest.log(Status.FAIL,error_str);

    }

}
