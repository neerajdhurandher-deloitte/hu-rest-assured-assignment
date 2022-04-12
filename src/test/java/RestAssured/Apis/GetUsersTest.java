package RestAssured.Apis;

import RestAssured.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
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


    @Test
    public void getUsers(){

        extentTest = extent.createTest("Get User Test");
        extentTest.log(Status.PASS,"Test setup");
        log.info("Get User Test setup");


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
        try {
            assertThat(flag, is(equalTo(valid_flag)));

            extentTest.log(Status.PASS, "Response contains male or female gender type users");
            log.info("Response contains male or female gender type users");

        }catch (AssertionError assertionError){

            log.error("Gender validation Error " + assertionError);
            extentTest.log(Status.PASS,"Gender validation Error " + assertionError);

            // this assert is add for show failed test case in console
            assertThat(flag, is(equalTo(valid_flag)));
        }

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
        }

        try{
            assertThat(requiredDomainCount,greaterThan(1));
            log.info(domain_check + " Domain validation Successful");
            extentTest.log(Status.PASS,domain_check + " Domain validation Successful");

        }catch (AssertionError assertionError){

            extentTest.log(Status.FAIL,"Domain validation error. Response contains less the 2 "+ domain_check + " domain. " + assertionError);
            log.error("Domain validation error. Response contains less the 2 "+ domain_check + " domain. " + assertionError);

            // this assert is add for show failed test case in console
            assertThat(requiredDomainCount,greaterThan(1));

        }

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

        try {
            assertThat(flag, is(equalTo(valid_flag)));
            log.info("Unique Id validation successful");
            extentTest.log(Status.PASS, "Unique Id validation successful");

        }catch (AssertionError assertionError){

            log.info("error :- id duplicate. " + assertionError);
            extentTest.log(Status.FAIL, "error :- id duplicate. " + assertionError);

            // this assert is add for show failed test case in console
            assertThat(flag, is(equalTo(valid_flag)));

        }


    }

}
