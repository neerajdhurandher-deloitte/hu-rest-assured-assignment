import Utils.UserSheetHandler;

import java.io.IOException;

public class tryFunction {

    public static void main(String[] args) throws IOException {

        UserSheetHandler sheetHandler = new UserSheetHandler("src/main/resources/UserDetails.xlsx");

        sheetHandler.readUserFile();

        sheetHandler.print_user_list();

    }
}
