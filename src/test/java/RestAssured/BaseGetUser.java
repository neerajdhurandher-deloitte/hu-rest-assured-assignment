package RestAssured;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.json.JSONArray;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BaseGetUser extends BaseTest{



    public void genderValidation(JSONArray jsonArray, ExtentTest extentTest) {

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

    public void domainValidation(JSONArray jsonArray, ExtentTest extentTest) {

        int requiredDomainCount = 0;
        String domain_check = getProperties().getProperty("domain_check"); // .biz

        for (int i = 0; i < jsonArray.length(); i++) {
            Object obj = jsonArray.getJSONObject(i).get("email");

            // dgsg@fafha.biz

            if (obj.toString().contains(domain_check)) {
                log.info(obj);
                requiredDomainCount++;
            }
        }



        if (requiredDomainCount < 2) {
            extentTest.log(Status.FAIL,"Domain validation error. Response contains less the 2 "+ domain_check + " domain");
            log.error("Domain validation error. Response contains less the 2 "+ domain_check + " domain");
        }else{
            log.info("Domain pass");
        }

    }

    public void idValidation(JSONArray jsonArray, ExtentTest extentTest) {

        ArrayList<Integer> id_list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            Object obj = jsonArray.getJSONObject(i).get("id");
            int id = Integer.parseInt(obj.toString());


            if(id_list.contains(id)){
                log.error("error");
                break;
            } else{
                log.info(id);
                id_list.add(id);
            }


        }


    }


}
