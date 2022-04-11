package Utils;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XlsxReader {
     String filePath;
     File file;
     FileInputStream inputStream;
     FileOutputStream outputStream;
     XSSFWorkbook workbook;
     XSSFSheet workbookSheet;

    public XlsxReader(String path) throws IOException {

        filePath = path;

        file = new File(filePath);

        inputStream = new FileInputStream(file);

        workbook = new XSSFWorkbook(inputStream);

        workbookSheet = workbook.getSheet("sheet1");

    }

}
