package RestAssured;


import Utils.User;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BaseTest{

    public ExtentSparkReporter extentSparkReporter;
    public static ExtentReports extent = new ExtentReports();
    public Logger log = LogManager.getLogger(BaseTest.class);

    static Properties properties = new Properties();
    FileInputStream inputStream;

    public static String base_url;
    public static String get_user_endpoint;
    public static String post_user_endpoint;
    public static String authToken;




    @BeforeTest
    public void testInit(){

        // extent report configuration setup
        extentSparkReporter = new ExtentSparkReporter("src/test/java/RestAssured/Reports/Test_Report.html");

        extentSparkReporter.config().setEncoding("utf-8");
        extentSparkReporter.config().setDocumentTitle("Assignment Report");
        extentSparkReporter.config().setReportName("Test Reports");
        extentSparkReporter.config().setTheme(Theme.DARK);


        extent.setSystemInfo("Organization", "Hashedin By Deloitte");
        extent.setSystemInfo("Created by ", "Neeraj Dhurandher");
        extent.attachReporter(extentSparkReporter);

        ExtentTest extentTest = extent.createTest("Base Test");
        extentTest.log(Status.PASS,"Test setup");
        log.info("Base test initialized");


        // read data from property file

        try {
            inputStream = new FileInputStream("src/main/resources/data.properties");
            properties.load(inputStream);
        }catch (FileNotFoundException e){
            extentTest.log(Status.FAIL, "Data Property file not found");
            log.error("Data Property file not found");
        } catch (IOException e) {
            e.printStackTrace();
            extentTest.log(Status.FAIL, e.getMessage());
            log.error(e.getMessage());
        }

        // get values from property file
        base_url = getProperties().getProperty("base_url");
        get_user_endpoint = getProperties().getProperty("get_user_endpoint");
        post_user_endpoint = getProperties().getProperty("post_user_endpoint");
        authToken = getProperties().getProperty("auth_token");

    }

    public Properties getProperties() {
        return properties;
    }

//    return basic request specification
    public RequestSpecification get_request_specification(){

        RequestSpecification requestSpecification = given().
                baseUri(base_url).
                header("Content-Type", "application/json").
                when();
        return requestSpecification;
    }

    // it validate status code, json schema in response & return response body
    protected Response responseValidation(Response response, int expectedStatusCode, String jsonSchemaPath){

        response = response.then().assertThat().body(JsonSchemaValidator.
                        matchesJsonSchema(
                                new File(jsonSchemaPath)))
                .statusCode(expectedStatusCode).contentType("application/json; charset=utf-8").extract().response();

        return response;
    }

    // it validates a value by using assertThat
    protected void assertValidation(int flag, int valid_flag, String successMsg, String errorMsg, ExtentTest extentTest) {

//        here we are trying to validate values using assertThat

        try {
            assertThat(flag, is(equalTo(valid_flag)));

            extentTest.log(Status.PASS, successMsg);
            log.info(successMsg);

        }catch (AssertionError assertionError){

            // if value is not valid it comes in catch part & show error log

            extentTest.log(Status.PASS,errorMsg + assertionError);
            log.error(errorMsg + assertionError);

            // this assert fails the test case in console
            assertThat(flag, is(equalTo(valid_flag)));
        }
    }

    // return a user map object
    public Map<String,String> getUserMapObj(User user){

        Map<String, String> userData = new HashMap<>();

        userData.put("name",user.getName());
        userData.put("email",user.getEmail());
        userData.put("gender",user.getGender());
        userData.put("status",user.getStatus());

        return userData;
    }



    @AfterTest
    public void afterTest(){
        log.info("Report generated.");
        extent.flush();
    }

}
