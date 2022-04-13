package RestAssured.Apis;

import RestAssured.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetUsersTest extends BaseTest {

    Response response;
    ExtentTest extentTest;
    JSONArray jsonArray;

    static final int valid_flag = 1;

    String successMsg;
    String errorMsg;


    @Test
    public void getUsers(){

        extentTest = extent.createTest("Get User Test");
        extentTest.log(Status.PASS,"Test setup");
        log.info("Get User Test setup");


        log.info("get user details end point hit");

//        hit the get end point
        response = super.get_request_specification().get(get_user_endpoint);

        // validate  status code, json schema in response
        response = super.responseValidation(response,200,"src/main/resources/JsonSchema/get_user_json_schema.json");

        // get array to response body's data filed
        jsonArray = new JSONArray(response.body().jsonPath().getList("data"));


        log.info(response.body().print());


    }


    @Test (priority = 2)
    public void genderValidation() {

        int flag = valid_flag;

        for (int i = 0; i < jsonArray.length(); i++) {
            Object obj = jsonArray.getJSONObject(i).get("gender");
            if (!obj.toString().equalsIgnoreCase("male") && !obj.toString().equalsIgnoreCase("female") ) {
                flag = 0;
                extentTest.log(Status.FAIL,"Gender error in Response" + obj);
                log.error("Gender error in Response"+ obj);
                break;
            }
        }

        successMsg = "Response contains male or female gender type users";
        errorMsg = "Gender validation Error ";


        super.assertValidation(flag, valid_flag, successMsg, errorMsg,extentTest);


    }

    @Test (priority = 2)
    public void domainValidation() {
        int requiredDomainCount = 0;
        String domain_check = getProperties().getProperty("domain_check");

        for (int i = 0; i < jsonArray.length(); i++) {
            Object obj = jsonArray.getJSONObject(i).get("email");

            if (obj.toString().contains(domain_check)) {
                requiredDomainCount++;
            }

            if(requiredDomainCount == 2)
                break;
        }

        successMsg = domain_check + " Domain validation Successful";
        errorMsg = "Domain validation error. Response contains less the 2 "+ domain_check + " domain. ";

        super.assertValidation(requiredDomainCount, 2, successMsg, errorMsg,extentTest);


    }

    @Test (priority = 2)
    public void idValidation() {

        ArrayList<Integer> id_list = new ArrayList<>();

        int flag = valid_flag;

        for (int i = 0; i < jsonArray.length(); i++) {

            Object obj = jsonArray.getJSONObject(i).get("id");
            int id = Integer.parseInt(obj.toString());

            if(id_list.contains(id)){
                flag = 0;
                break;
            }else{
                id_list.add(id);
            }

        }

        successMsg = "Unique Id validation successful";
        errorMsg = "error :- id duplicate. ";

        super.assertValidation(flag, valid_flag, successMsg, errorMsg,extentTest);



    }

}
