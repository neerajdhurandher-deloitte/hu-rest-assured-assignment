package RestAssured.Apis;

import RestAssured.BaseTest;
import Utils.ExcelReader;
import Utils.User;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PostUserTest extends BaseTest {

    Response response;
    ExtentTest extentTest;
    Map<String,String> post_data;

    public static ExcelReader excelReader;
    public static ArrayList<User> userList = new ArrayList<>();



    @Test(priority = 1)
    public void readDataFromExcel(){

        extentTest = extent.createTest("Post User Test");
        extentTest.log(Status.PASS,"Post User Test setup");
        log.info("Post User Test setup");


        // read data from xlsx sheet

        String fileReadSuccessfulMsg = "successfully read user list from excel sheet";
        String fileReadUnsuccessfulMsg = "read user list from excel sheet unsuccessful";

        try {
            // get all users list from xlsx sheet
            excelReader = new ExcelReader("src/main/resources/UserDetails.xlsx");
            userList = excelReader.readUserFile();
            extentTest.log(Status.PASS,fileReadSuccessfulMsg);
            log.info(fileReadSuccessfulMsg);

        }catch (IOException e){
            log.error(e.getMessage());
            e.printStackTrace();
            extentTest.log(Status.FAIL,fileReadUnsuccessfulMsg);
            log.error(fileReadUnsuccessfulMsg);
        }
    }

    @Test(priority = 2)
    public void postUser(){

        post_data = getUser();

        log.info("post user details end point hit");

        try {


            response = given().
                    baseUri(base_url).
                    header("Content-Type", "application/json").
                    header("Authorization", "Bearer " + authToken).
                    body(post_data).
                    when().
                    post(post_user_endpoint).
                    then().assertThat().body(JsonSchemaValidator.
                            matchesJsonSchema(
                                    new File("src/main/resources/JsonSchema/post_user_json_schema.json")))
                    .statusCode(201).contentType("application/json; charset=utf-8").extract().response();

            log.info(response.body().print());
            extentTest.log(Status.PASS,response.body().print());

            validate_response();

        }catch (AssertionError assertionError){
            log.error(assertionError);

            response = given().
                    baseUri(base_url).
                    header("Content-Type", "application/json").
                    header("Authorization", "Bearer " + authToken).
                    body(post_data).
                    when().
                    post(post_user_endpoint).
                    then().assertThat().body(JsonSchemaValidator.
                            matchesJsonSchema(
                                    new File("src/main/resources/JsonSchema/post_error_json_schema.json")))
                    .statusCode(422).contentType("application/json; charset=utf-8").extract().response();

            readErrorMsg();
        }

    }

    private void validate_response() {

        boolean name_validation = response.body().jsonPath().getString("data.name").equals(post_data.get("name"));
        boolean email_validation = response.body().jsonPath().getString("data.email").equals(post_data.get("email"));
        boolean gender_validation = response.body().jsonPath().getString("data.gender").equals(post_data.get("gender"));
        boolean status_validation = response.body().jsonPath().getString("data.status").equals(post_data.get("status"));

        try {

            assertThat(name_validation, is(equalTo(true)));
            assertThat(email_validation, is(equalTo(true)));
            assertThat(gender_validation, is(equalTo(true)));
            assertThat(status_validation, is(equalTo(true)));

            log.info("Response is valid");
            extentTest.log(Status.PASS, "Response is valid");

        }catch (AssertionError assertionError){

            log.info("Invalid Response " + assertionError);
            extentTest.log(Status.PASS,"Invalid Response " +assertionError);

            // this assert is add for show failed test case in console
            assertThat(name_validation, is(equalTo(true)));
            assertThat(email_validation, is(equalTo(true)));
            assertThat(gender_validation, is(equalTo(true)));
            assertThat(status_validation, is(equalTo(true)));

        }

    }

    public void readErrorMsg(){

        String error_message = response.body().jsonPath().getString("data.message");
        String error_field = response.body().jsonPath().getString("data.field");

        String error_str = "Error Message :- "+ error_field+ " " +error_message;

        log.error(response.body().toString());
        log.error(error_str);
        extentTest.log(Status.FAIL,error_str);

    }

    public Map<String,String> getUser(){

        User user = userList.get(0);

        Map<String, String> userData = new HashMap<>();

        userData.put("name",user.getName());
        userData.put("email",user.getEmail());
        userData.put("gender",user.getGender());
        userData.put("status",user.getStatus());

        return userData;
    }


}
