package RestAssured.Apis;

import RestAssured.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GetUsersTest extends BaseTest {

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

        int flag = 1;
        for (int i = 0; i < jsonArray.length(); i++) {
            Object obj = jsonArray.getJSONObject(i).get("gender");
            if (!obj.toString().equals("male") && !obj.toString().equals("female") ) {
                flag = 0;
                extentTest.log(Status.FAIL,"Gender error in Response" + obj);
                log.error("Gender error in Response"+ obj);
                break;
            }
        }

        assertThat(flag, is(equalTo(1)));
        extentTest.log(Status.PASS,"Response contains male or female gender type users");
        log.info("Response contains male or female gender type users");

    }

    @Test (priority = 2)
    public void domainValidation() {
        int requiredDomainCount = 0;
        String domain_check = getProperties().getProperty("domain_check");

        for (int i = 0; i < jsonArray.length(); i++) {
            Object obj = jsonArray.getJSONObject(i).get("email");

            if (obj.toString().contains(domain_check)) {
                log.info(obj);
                requiredDomainCount++;
            }
        }



        if (requiredDomainCount < 2) {
            extentTest.log(Status.FAIL,"Domain validation error. Response contains less the 2 "+ domain_check + " domain");
            log.error("Domain validation error. Response contains less the 2 "+ domain_check + " domain");
        }

    }

    @Test (priority = 2)
    public void idValidation() {

        ArrayList<Integer> id_list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            Object obj = jsonArray.getJSONObject(i).get("id");
            int id = Integer.parseInt(obj.toString());


            if(id_list.contains(id)){
                log.info("error");
                break;
            } else{
                log.info(id);
                id_list.add(id);
            }


        }


    }

}
