package Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:locator.feature"},
        glue = {"acceptancetests"},
        plugin = {"pretty",
                "html:target/reports.html",



        }
)
public class TestRunner {
}
