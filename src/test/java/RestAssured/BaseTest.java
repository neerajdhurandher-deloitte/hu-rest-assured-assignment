package RestAssured;

import Utils.User;
import Utils.UserSheetHandler;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

public class BaseTest {
    public static UserSheetHandler userSheetHandler;
    public ArrayList<User> userList = new ArrayList<>();

    public ExtentSparkReporter extentSparkReporter;
    public ExtentReports extent = new ExtentReports();
    public Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeTest
    public void testInit(){

        // extent report setup
        extentSparkReporter = new ExtentSparkReporter("src/test/java/RestAssured/Reports/Test_Report.html");

        extentSparkReporter.config().setEncoding("utf-8");
        extentSparkReporter.config().setDocumentTitle("Main Assignment Report");
        extentSparkReporter.config().setReportName("Test Reports");
        extentSparkReporter.config().setTheme(Theme.DARK);


        extent.setSystemInfo("Organization","Hashedin By Deloitte");
        extent.setSystemInfo("Created by ","Neeraj Dhurandher");
        extent.attachReporter(extentSparkReporter);

        ExtentTest extentTest = extent.createTest("Base Test");
        extentTest.log(Status.PASS,"Test setup");
        log.info("Base test initialized");

        // read data from xlsx sheet

        try {
            // get all users list from xlsx sheet
            userSheetHandler = new UserSheetHandler("src/main/resources/UserDetails.xlsx");
            userList = userSheetHandler.readUserFile();
            extentTest.log(Status.PASS,"successfully read user list");
            log.info("successfully read user list");

        }catch (IOException e){
            log.error(e.getMessage());
            e.printStackTrace();
            extentTest.log(Status.FAIL,"read user list unsuccessful");
            log.error("read user list unsuccessful");
        }
    }

    @Test
    public void trail(){
        ExtentTest extentTest = extent.createTest("Trail Test");
        extentTest.log(Status.PASS,"Trail report item");
        log.info("tail log");

    }

    @AfterTest
    public void afterTest(){
        extent.flush();
    }

}
