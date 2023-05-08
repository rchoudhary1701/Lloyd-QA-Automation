package acceptancetests;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;


import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.ExcelReader;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LocationFinder {

    static WebDriver driver;

    Logger logger
            = Logger.getLogger(
            LocationFinder.class.getName());

    public void loadDriver() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        this.driver = driver;
    }


    public static void waitForElement(String element, int timeOut) {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
    }

    public static void waitForElementLessThan(String element, int timeOut, int numberOfElements) {
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath(element), numberOfElements));
    }

    @Given("user navigates to url")
    public void user_navigates_to() {
        this.loadDriver();
        driver.manage().window().maximize();
        driver.get("https://www.lloydsbank.com/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(35));


    }

    @And("user clicks on BranchFinder icon")
    public void userClicksOnBranchFinderIcon() {
        waitForElement("//*[@id='nav-header']//*[text()='Branch Finder']/ancestor::a", 5);
        driver.findElement(By.xpath("//*[@id='nav-header']//*[text()='Branch Finder']/ancestor::a")).click();

    }

    @Given("User fills the Location finder with sheetname {string} and rownumber {int}")
    public void user_fills_the_location_finder_with_sheetname_and_rownumber(String BranchLocationFinder, Integer rownumber) throws IOException, InvalidFormatException, InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(75));

        driver.manage().deleteAllCookies();
        ExcelReader excelReader = new ExcelReader();
        List<Map<String, String>> testData = excelReader.getData(".\\src\\datarequired\\BranchLocationFinder.xlsx", BranchLocationFinder);
        String heading = testData.get(rownumber).get("PostCode");

        waitForElement("//input[@name='qp']", 5);
        WebElement search_by_postcode = driver.findElement(By.xpath("//input[@name='qp']"));
        search_by_postcode.clear();
        search_by_postcode.sendKeys(heading.trim());
        try {
            waitForElementLessThan("//*[@id='results']//*[@role='option']", 5, 2);
        } catch (Exception e){
            search_by_postcode.sendKeys(" ");
            waitForElementLessThan("//*[@id='results']//*[@role='option']", 5, 2);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(75));
        waitForElement("//*[@id='results']//*[@role='option']", 5);
        driver.findElement(By.xpath("//*[@id='results']//*[@role='option']")).click();
        driver.findElement(By.xpath("//html")).click();
    }

    @When("User clicks on the Branch location Finder button")
    public void user_clicks_on_the_branch_location_finder_button() {
        waitForElement("(//*[@class='Teaser-links']//*[text()='View branch '])[1]", 5);
        WebElement firstList = driver.findElement(By.xpath("(//*[@class='Teaser-links']//*[text()='View branch '])[1]"));
        firstList.click();
    }

    @Then("User able to fetch the Phone Number of nearest Branch and get displayed")
    public void user_able_to_fetch_the_phone_number_of_nearest_branch_and_get_displayed() {

        String textReceived = driver.findElement(By.id("phone-main")).getText();
        logger.log(Level.INFO, textReceived);
        driver.close();
        driver.quit();


    }


}
