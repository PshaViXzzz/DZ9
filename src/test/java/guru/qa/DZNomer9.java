package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import model.ExampleJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class DZNomer9 {
    private ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    void pdfFileInZipTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                Objects.requireNonNull(cl.getResourceAsStream("example.7z"))
        )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("printedForm.pdf")) {
                    PDF pdf = new PDF(zis);
                    assertThat(pdf.text).contains("ОСФР ПО РЕСПУБЛИКЕ МОРДОВИЯ");
                }
                ;
            }
        }
    }


    @Test
    void xlsFileInZipTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("example.7z")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("Справочник типов документов")) {
                    XLS xls = new XLS(zis);
                    String actualValue = xls.excel.getSheetAt(0).getRow(6).getCell(1).getStringCellValue();
                    assertThat(actualValue).isEqualTo("Решение суда");
                }
            }
        }
    }

    @Test
    void csvFileInZipTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("example.7z")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if(entry.getName().equals("Новый текстовый документ")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
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
        }
    }
    @Test
    void jsonParcingDataTest() throws Exception {
        try (InputStream inputStream = cl.getResourceAsStream("exampleJson.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ExampleJson user = objectMapper.readValue(inputStream, ExampleJson.class);
            Assertions.assertEquals("Pavel", user.getFirstname());
            Assertions.assertEquals("Volchenkov", user.getLastname());
            Assertions.assertEquals("qa", user.getRole());
            Assertions.assertEquals("25", user.getAge());        }
    }
}