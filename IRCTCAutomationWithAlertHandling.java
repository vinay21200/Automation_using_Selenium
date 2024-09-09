package org.apache.maven.Selo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class IRCTCAutomationWithAlertHandling {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        // Set up ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        
        // Initialize WebDriver
        driver = new ChromeDriver();
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        // Open IRCTC Train Search page
        driver.get("https://www.irctc.co.in/nget/train-search");
        
        // Maximize browser window
        driver.manage().window().maximize();
    }

    @Test
    public void testIRCTCSearchWithAlertHandling() {
        // Wait for the page to load fully
        try {
            Thread.sleep(5000); // It's better to use WebDriverWait instead of Thread.sleep
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Handle pop-up if it appears
        try {
            WebElement popup = driver.findElement(By.xpath("//button[contains(@class, 'btn btn-primary')]"));
            Assert.assertTrue(popup.isDisplayed(), "Pop-up is not displayed!");
            popup.click();
        } catch (Exception e) {
            System.out.println("No pop-up displayed.");
            Assert.assertTrue(true, "No pop-up found, proceeding with the test.");
        }

        // Enter "From" station using consistent XPath
        WebElement fromStation = driver.findElement(By.xpath("//input[@role='searchbox' and @placeholder='From*']"));
        fromStation.click();
        fromStation.sendKeys("NDLS");  // New Delhi

        // Enter "To" station using consistent XPath and replace "BTC" with "GKP" (Gorakhpur)
        WebElement toStation = driver.findElement(By.xpath("//input[@role='searchbox' and @placeholder='To*']"));
        toStation.click();
        toStation.sendKeys("GKP");  // Gorakhpur

        // Select the Journey Date: 9th September 2024 using consistent XPath
        WebElement journeyDate = driver.findElement(By.xpath("//input[@id='jDate']"));
        journeyDate.click();

        WebElement nextButton = driver.findElement(By.xpath("//a[@title='Next']"));
        WebElement monthYearDisplay = driver.findElement(By.xpath("//div[contains(@class, 'ui-datepicker-title')]"));
        
        // Loop until September 2024 is displayed using consistent XPath
        while (!monthYearDisplay.getText().contains("September 2024")) {
            nextButton.click();
        }
        WebElement selectDate = driver.findElement(By.xpath("//a[text()='9']"));
        selectDate.click();

        // Click the dropdown to choose class (Sleeper SL) using consistent XPath
        WebElement classDropdown = driver.findElement(By.xpath("//span[contains(text(),'All Classes')]"));
        classDropdown.click();
        
        // Select "Sleeper (SL)" from the dropdown list using consistent XPath
        WebElement sleeperOption = driver.findElement(By.xpath("//li/span[text()='Sleeper (SL)']"));
        sleeperOption.click();

        // Verify Sleeper (SL) was selected using consistent XPath
        WebElement selectedClass = driver.findElement(By.xpath("//span[contains(text(),'Sleeper (SL)')]"));
        Assert.assertTrue(selectedClass.isDisplayed(), "Sleeper (SL) is not selected!");

        // Click the 'Search' button using consistent XPath
        WebElement searchButton = driver.findElement(By.xpath("//button[contains(text(),'Search')]"));
        searchButton.click();

        // Wait for search results and handle alert
        try {
            Thread.sleep(5000); // It's better to use WebDriverWait here
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Handle the confirmation alert that appears when no direct trains are available
        try {
            WebElement alertYesButton = driver.findElement(By.xpath("//button[text()='Yes']"));
            WebElement alertNoButton = driver.findElement(By.xpath("//button[text()='No']"));
            
            if (alertNoButton.isDisplayed()) {
                alertNoButton.click();
                System.out.println("No direct trains available, clicked 'No'.");
            }
        } catch (Exception e) {
            System.out.println("No alert present, proceeding without clicking 'No'.");
        }
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        driver.quit();
    }
}
