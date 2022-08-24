package com.test;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;

public class FirstTest {

    private WebDriver driver;
    private String model = "Lenovo";
    private int minPrice = 25000;
    private int maxPrice = 30000;

    @BeforeTest
    public void before() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
        driver = new ChromeDriver();
        driver.get("https://market.yandex.ru/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    @AfterTest
    public void after() {
        driver.close();
    }

    @Test
    public void testest() {

        WebElement catalogue = driver.findElement(By.id("catalogPopupButton"));
        catalogue.click();

        WebElement computer = driver.findElement((By.xpath("//a[@class='_191Hm'][contains(@href, '/catalog--kompiuternaia-tekhnika/54425')]")));
        new Actions(driver)
                .moveToElement(computer)
                .perform();

        WebElement laptops = driver.findElement(By.xpath("//a[@href='/catalog--noutbuki/54544/list?hid=91013']"));
        laptops.click();

        WebElement showAllManufacturers = driver.findElement(By.xpath("//*[contains(text(), 'Показать всё')]/.."));
        showAllManufacturers.click();

        WebElement inputModel = driver.findElement(By.xpath("//label[contains(text(),'Найти производителя')]/../input"));
        inputModel.sendKeys(model);

        WebElement acceptModel = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/div[1]/div/div[5]/div/div/div/div/div/div[2]/div/div[4]/div/div/div/div/div[4]/div/fieldset/div/div/div[2]/div/div/div/div/label"));
        acceptModel.click();

        WebElement inputCostValue = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/div[1]/div/div[5]/div/div/div/div/div/div[2]/div/div[4]/div/div/div/div/div[1]/div/fieldset/div/div/div/span[1]/div/div/input"));
        inputCostValue.sendKeys(Integer.toString(minPrice), Keys.TAB, Integer.toString(maxPrice));

        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[@aria-label='Загрузка...']"))));

        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//span[@aria-label='Загрузка...']"))));

        driver.findElements(By.xpath("//a[@data-baobab-name='title']/span")).forEach(webElement -> {
            Assert.assertTrue(webElement.getText().contains(model), "Неправильная поисковая выдача");
        });

        driver.findElements(By.xpath("//div[@data-baobab-name='price']//span[@data-auto='mainPrice']/span[1]")).forEach(webElement -> {
            int price = Integer.parseInt(webElement.getText()
                    .replaceAll(" ", ""));

            Assert.assertTrue(price <= maxPrice, "Цена больше запрашиваемой");
            Assert.assertTrue(minPrice <= price, "Цена ниже запрашиваемой");
        });

        System.out.println("Поиск прошёл успешно");

    }
}
