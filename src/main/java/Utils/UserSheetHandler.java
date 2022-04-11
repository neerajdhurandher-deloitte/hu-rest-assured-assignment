package Utils;

import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.util.ArrayList;

public class UserSheetHandler extends ExcelReader {

    ArrayList<User> userList = new ArrayList<>();


    public UserSheetHandler(String path) throws IOException {
        super(path);
    }

    public ArrayList<User> readUserFile() throws IOException {

        int rowCount = workbookSheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowCount; i++) {

            Row row = workbookSheet.getRow(i);

            int cellCount = row.getLastCellNum();

            User user = new User();


            for (int j = 0; j < cellCount; j++) {

                String data = "";
                try {
                    data = row.getCell(j).getStringCellValue();
                }catch (Exception e){

                }

                switch (j){
                    case 0-> user.setName(data);
                    case 1-> user.setGender(data);
                    case 2-> user.setEmail(data);
                    case 3-> user.setStatus(data);
                }
            }

            userList.add(user);
        }

        return userList;
    }

    public void print_user_list() {

        for (User user : userList ){
            System.out.println("Name :- " + user.getName());
            System.out.println("Gender :- " + user.getGender());
            System.out.println("Email :- " + user.getEmail());
            System.out.println("Status :- " + user.getStatus());
            System.out.println();
        }
    }
}
