package guru.qa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class SelenideFilesTest {

    @Test
    void downloadFileTest() throws Exception {
      open("https://github.com/junit-team/junit5/blob/main/README.md");
      File downloaded =
              $(".react-blob-header-edit-and-raw-actions [href*='main/README.md']")
                      .download();

        try (InputStream is = new FileInputStream(downloaded)) {
          byte[] data = is.readAllBytes();
          String dataAsString = new String(data, StandardCharsets.UTF_8);
            Assertions.assertTrue(dataAsString.contains("Contributions to JUnit 5 are both welcomed and appreciated"));
        }
    }

    @Test
    void uploadFileTest() {
        open("https://file.com.ru/");
        $("input[type='file']").uploadFromClasspath("TEST_IMAGE.jpg");
        $(".dz-name").shouldHave(text("TEST_IMAGE.jpg"));
    }

}
