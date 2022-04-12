import Utils.ExcelReader;

import java.io.IOException;

public class tryFunction {

    public static void main(String[] args) throws IOException {

        ExcelReader excelReader = new ExcelReader("src/main/resources/UserDetails.xlsx");

        excelReader.readUserFile();

        excelReader.print_user_list();

    }
}
