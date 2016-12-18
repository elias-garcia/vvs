package es.udc.pa.pa015.practicapa.test.web;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;

public class AdminWebTest {

	/*
	 * -Crear enventos -Añadir betType a eventos -Seleccionar ganadoras
	 */

	private static String APP_URL = "http://localhost:9090/betting-app/";
	private static String ADMIN_NAME = "admin";
	private static String ADMIN_PASSWORD = "admin";
	private static String EVENT_NAME1 = "Deportivo - Celta";
	private static String EVENT_STARTDATE1 = "2017-01-30";
	private static String EVENT_STARTDATE1_FIREFOXFORMAT = "30/01/17";
	private static String EVENT_STARTTIME1 = "18:00";
	private static String EVENT_CATEGORY1 = "Fútbol";
	private static String BETTYPE_QUESTION1 = "¿Ganador?";
	private static String BETTYPE_ISMULTIPLE = "Yes";
	private static String BETTYPE_NOTISMULTIPLE = "No";
	private static String TYPEOPTION_RESULT = "Deportivo";
	private static String TYPEOPTION_ODD = "1";

	// Versión de Selenium 2.53
	WebDriver driver;
	WebDriverWait driverWait;

	// @BeforeMethod
	public void firefoxDriver() {
		// Versión de Firefox 46
		driver = new FirefoxDriver();
		driverWait = new WebDriverWait(driver, 5);
	}

	public void adminLogin() {
		driver.get(APP_URL);
		driver.findElement(By.id("authenticationButton")).click();
		WebElement inputLoginName = driver.findElement(By.id("loginName"));
		inputLoginName.sendKeys(ADMIN_NAME);
		driver.findElement(By.id("password")).sendKeys(ADMIN_PASSWORD);
		inputLoginName.submit();
	}

	// @Test(priority = 0)
	public void testLoginAdminWeb() throws InterruptedException {
		adminLogin();

		AssertJUnit.assertEquals(ADMIN_NAME, driver.findElement(By.id("userNameButton")).getText());

		driver.quit();
	}

	// @Test(priority = 1)
	public void testCreateAndFindEventsWeb() throws InterruptedException {

		adminLogin();

		driver.findElement(By.id("addEventButton")).click();

		driver.findElement(By.id("eventName")).sendKeys(EVENT_NAME1);
		driver.findElement(By.id("startDate")).sendKeys(EVENT_STARTDATE1);
		driver.findElement(By.id("startTime")).sendKeys(EVENT_STARTTIME1);
		WebElement wannabeSelect = driver.findElement(By.id("categoryId"));
		Select category = new Select(wannabeSelect);
		category.selectByVisibleText(EVENT_CATEGORY1);

		wannabeSelect.submit();

		// wait until the button to go home page is shown
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("backToHomeEventAddedButton")));
		driver.findElement(By.id("backToHomeEventAddedButton")).click();

		// Check the button to go to home page works
		AssertJUnit.assertEquals(APP_URL, driver.getCurrentUrl());

		// Check the event is created
		driver.findElement(By.id("searchEventsButton")).click();

		WebElement keywordsInput = driver.findElement(By.id("keywords"));
		keywordsInput.sendKeys(EVENT_NAME1);

		keywordsInput.submit();

		List<WebElement> events = driver.findElements(By.xpath("//td"));

		AssertJUnit.assertEquals(events.get(0).getText(), EVENT_NAME1);
		AssertJUnit.assertEquals(events.get(1).getText(), EVENT_CATEGORY1);
		AssertJUnit.assertEquals(events.get(2).getText(), EVENT_STARTDATE1_FIREFOXFORMAT + " - " + EVENT_STARTTIME1);

		driver.quit();
	}

	// @Test(priority = 2)
	public void testAddBetTypesWeb() throws InterruptedException {

		adminLogin();

		driver.findElement(By.id("searchEventsButton")).click();

		driver.findElement(By.id("keywords")).submit();

		List<WebElement> events = driver.findElements(By.xpath("//td"));

		events.get(0).findElement(By.id("eventDetailsButton")).click();

		// Thread.sleep(300000);

		driver.findElement(By.id("addBetTypeButton")).click();

		driver.findElement(By.id("betTypeQuestion")).sendKeys(BETTYPE_QUESTION1);

		WebElement wannabeSelect = driver.findElement(By.id("multipleType"));
		Select multiple = new Select(wannabeSelect);
		multiple.selectByVisibleText(BETTYPE_NOTISMULTIPLE);

		Thread.sleep(1000);

		driver.findElement(By.xpath("//a[contains(@data-afl-trigger,'add')]")).click();

		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("backToHomeEventAddedButton")));

		WebElement typeOptionResult = driver.findElement(By.xpath("//*[contains(@id,'result')]"));

		typeOptionResult.sendKeys(TYPEOPTION_RESULT);

		WebElement typeOptionOdd = driver.findElement(By.xpath("//*[contains(@id,'odd')]"));

		typeOptionOdd.sendKeys(TYPEOPTION_ODD);

		Thread.sleep(1000);

		// driver.findElement(By.id("addEventButton")).click();
		//
		// driver.findElement(By.id("eventName")).sendKeys(EVENT_NAME1);
		// driver.findElement(By.id("startDate")).sendKeys(EVENT_STARTDATE1);
		// driver.findElement(By.id("startTime")).sendKeys(EVENT_STARTTIME1);
		// WebElement wannabeSelect = driver.findElement(By.id("categoryId"));
		// Select category = new Select(wannabeSelect);
		// category.selectByVisibleText(EVENT_CATEGORY1);
		//
		// wannabeSelect.submit();
		//
		// // wait until the button to go home page is shown
		// driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("backToHomeEventAddedButton")));
		// driver.findElement(By.id("backToHomeEventAddedButton")).click();
		//
		// // Check the button to go to home page works
		// assertEquals(APP_URL, driver.getCurrentUrl());
		//
		// // Check the event is created
		// driver.findElement(By.id("searchEventsButton")).click();
		//
		// WebElement keywordsInput = driver.findElement(By.id("keywords"));
		// keywordsInput.sendKeys(EVENT_NAME1);
		//
		// keywordsInput.submit();
		//
		// List<WebElement> events = driver.findElements(By.xpath("//td"));
		//
		// assertEquals(events.get(0).getText(), EVENT_NAME1);
		// assertEquals(events.get(1).getText(), EVENT_CATEGORY1);
		// assertEquals(events.get(2).getText(), EVENT_STARTDATE1_FIREFOXFORMAT
		// + " - " + EVENT_STARTTIME1);

		driver.quit();
	}

}
