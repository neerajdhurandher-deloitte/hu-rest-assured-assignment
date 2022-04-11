package RestAssured;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.response.Response;

import java.util.Map;

public class BasePostUser extends BaseTest{

    public void validate_response(Response response, ExtentTest extentTest, Map<String, String> post_data) {

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

    public void readErrorMsg(Response response, ExtentTest extentTest){

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
