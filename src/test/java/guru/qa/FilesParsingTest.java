package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.ex.Strings;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.*;

public class FilesParsingTest {
    private ClassLoader cl = FilesParsingTest.class.getClassLoader();
    @Test
    void pdfFileParsingTest() throws Exception{
        open("https://junit.org/junit5/docs/current/user-guide/");
        File downloaded = $("[href*='junit-user-guide-5.11.1.pdf']").download();
        try (InputStream is = new FileInputStream(downloaded)) {
            PDF pdf = new PDF(downloaded);
            System.out.println();
        }
    }

    @Test
    void xlsFileParsingTest() throws Exception {
       open("https://excelvba.ru/programmes/Teachers");
       File downloaded = $("[href='https://ExcelVBA.ru/sites/default/files/teachers.xls']").download();
        XLS xls = new XLS(downloaded);

        String actualValue = xls.excel.getSheetAt(0).getRow(3).getCell(2).getStringCellValue();


        System.out.println();

    }

    @Test
    void csvFileParsingTest() throws Exception{
        try(InputStream is = cl.getResourceAsStream("example.csv");
            CSVReader csvReader = new CSVReader(new InputStreamReader(is))){

                List<String[]> data = csvReader.readAll();
                Assertions.assertEquals(2, data.size());
                Assertions.assertArrayEquals(
                        new String[] {"Selenide", "https://selenide.org"},
                        data.get(0)
                );
                Assertions.assertArrayEquals(
                        new String[] {"JUnit 5", "https://junit.org"},
                        data.get(1)
                );
        }
    }

    @Test
    void zipFileParsingTest() throws Exception{
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("example.7z")
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
             System.out.println(entry.getName());
            }
        }
    }
}
