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

        post_data = getUserMapObj(userList.get(3));

        log.info("post user details end point hit");

        response = super.get_request_specification().header("Authorization", "Bearer " + authToken).body(post_data).post(post_user_endpoint);

        // validate  status code, json schema in response

        try {

            response = super.responseValidation(response,201,"src/main/resources/JsonSchema/post_user_json_schema.json");

            log.info(response.body().print());
            extentTest.log(Status.PASS,response.body().print());

            validate_response();

        }catch (AssertionError assertionError){

            log.error(assertionError);

            response = super.responseValidation(response,422,"src/main/resources/JsonSchema/post_error_json_schema.json");

            readErrorMsg();
        }

    }

    private void validate_response() {

        boolean name_validation = stringMatcher("data.name", "name");
        boolean email_validation = stringMatcher("data.email","email");
        boolean gender_validation = stringMatcher("data.gender","gender");
        boolean status_validation = stringMatcher("data.status","status");

        try {

            assertValidation(name_validation,email_validation,gender_validation,status_validation);

            log.info("Response is valid");
            extentTest.log(Status.PASS, "Response is valid");

        }catch (AssertionError assertionError){

            log.info("Invalid Response " + assertionError);
            extentTest.log(Status.PASS,"Invalid Response " +assertionError);

            // this assert is add for show failed test case in console

            assertValidation(name_validation,email_validation,gender_validation,status_validation);

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


    public boolean stringMatcher(String path, String filed){
        return  response.body().jsonPath().getString(path).equals(post_data.get(filed));
    }

    private void assertValidation(boolean name_validation, boolean email_validation, boolean gender_validation, boolean status_validation) {
        assertThat(name_validation, is(equalTo(true)));
        assertThat(email_validation, is(equalTo(true)));
        assertThat(gender_validation, is(equalTo(true)));
        assertThat(status_validation, is(equalTo(true)));
    }





}
