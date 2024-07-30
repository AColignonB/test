import java.io.*;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.util.Arrays;
public class Utils {
    /*
    public void screenshot(String funcionalidad) throws IOException{
        File screenshotFile1=((TakesScreenshot)CarroCompraTest.mobiledriver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile1,new File("Evidencia/"+funcionalidad+"-"+GetTimeStampValue()+".png"));

    }

    public String GetTimeStampValue() throws IOException {
        Calendar cal = Calendar.getInstance();
        Date time = cal.getTime();
        String timestamp = time.toString();
        System.out.println(timestamp);
        String systime = timestamp.replace(":", "-");
        System.out.println(systime);
        return systime;
    }
    */

    public static String[][] getExcelData(String fileName, String sheetName) {

        String[][] arrayExcelData = null;
        String encodeOrigen = "UTF-8";
        String encodeDestino = "UTF-8";
        try {
            FileInputStream fs = new FileInputStream(fileName);
            Workbook wb = Workbook.getWorkbook(fs);
            Sheet sh = wb.getSheet(sheetName);

            int totalNoOfCols = sh.getColumns();
            int totalNoOfRows = sh.getRows();

            arrayExcelData = new String[totalNoOfRows-1][totalNoOfCols];

            for (int i= 1 ; i < totalNoOfRows; i++) {

                for (int j=0; j < totalNoOfCols; j++) {
                    arrayExcelData[i-1][j] = StringCoding((sh.getCell(j, i).getContents()), encodeOrigen, encodeDestino);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        //System.out.println(StringCoding(s, encodeOrigen, encodeDestino));
        return arrayExcelData;
    }

    public static String StringCoding(String s, String encodeOrigen, String encodeDestino) {
        try {
            String str = new String(s.getBytes(encodeOrigen), encodeDestino);
            return str;
        } catch (Exception e) {
            return s;
        }
    }

    public static byte[] getBytes(String input) {
        try {
            return input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException uee) {
            return input.getBytes(); // default encoding
        }
    }


}