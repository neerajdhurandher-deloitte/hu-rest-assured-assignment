package RestAssured;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class BaseTest{


    public ExtentSparkReporter extentSparkReporter;
    public ExtentReports extent = new ExtentReports();
    public Logger log = LogManager.getLogger(BaseTest.class);

    Properties properties = new Properties();
    FileInputStream inputStream;

    public static String base_url;
    public static String get_user_endpoint;
    public static String post_user_endpoint;
    public static String authToken;

    public RequestSpecification requestSpecification;
    public RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    public ResponseSpecification responseSpecification;
    public ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();


    @BeforeTest
    public void testInit(){

        // extent report setup
        extentSparkReporter = new ExtentSparkReporter("src/test/java/RestAssured/Reports/Test_Report.html");

        extentSparkReporter.config().setEncoding("utf-8");
        extentSparkReporter.config().setDocumentTitle("Assignment Report");
        extentSparkReporter.config().setReportName("Test Reports");
        extentSparkReporter.config().setTheme(Theme.DARK);


        extent.setSystemInfo("Organization","Hashedin By Deloitte");
        extent.setSystemInfo("Created by ","Neeraj Dhurandher");
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

        base_url = getProperties().getProperty("base_url");
        get_user_endpoint = getProperties().getProperty("get_user_endpoint");
        post_user_endpoint = getProperties().getProperty("post_user_endpoint");
        authToken = getProperties().getProperty("auth_token");


    }

    public Properties getProperties() {
        return properties;
    }


    public RequestSpecBuilder getRequestSpecBuilder() {
        return requestSpecBuilder.setBaseUri(base_url).setContentType(ContentType.JSON);
    }

    public ResponseSpecBuilder getResponseSpecBuilder() {
        return responseSpecBuilder.expectContentType(ContentType.JSON);
    }

    @AfterTest
    public void afterTest(){
        log.info("Report generated");
        extent.flush();
    }

}
