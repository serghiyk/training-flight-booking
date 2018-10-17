import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeSuite;

import java.net.MalformedURLException;

public class ConfigurationFile {
    @BeforeSuite
    public void setUp() throws MalformedURLException {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        Configuration.baseUrl = "https://www.expedia.com";
        Configuration.browser = "chrome";
    }
}
