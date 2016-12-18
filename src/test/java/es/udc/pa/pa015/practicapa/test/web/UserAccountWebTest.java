package es.udc.pa.pa015.practicapa.test.web;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class UserAccountWebTest {

	// Versión de selenium 2.53
	// Versión de Firefox 46

	private static String userLoginName = "user";
	private static String userPassword = "demo";

	// @Test
	public void testLoginFirefox() throws InterruptedException {

		/* Creamos una instancia del driver de Firefox */
		WebDriver driver = new FirefoxDriver();

		/* Creamos una instancia del driver de Firefox */
		driver.get("http://localhost:9090/betting-app");

		/* Pulsamos el boton de autentificación */
		driver.findElement(By.id("authenticationButton")).click();

		/* Nos logueamos */
		WebElement inputLoginName = driver.findElement(By.id("loginName"));
		inputLoginName.sendKeys(userLoginName);

		driver.findElement(By.id("password")).sendKeys(userPassword);

		inputLoginName.submit();

		/* Comprobamos que los datos visualizados son correctos */
		assertEquals(userLoginName, driver.findElement(By.id("userNameButton")).getText());

		driver.quit();

	}

}
