import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;
import org.testng.annotations.DataProvider;


public class LoginTest extends BaseTest{

    static AndroidDriver mobiledriver = null;

    @BeforeTest
    public void setUp() throws IOException {
    	startAppiumServer();
    	DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("appium:automationName", "UIAutomator2");
		capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("appium:app", new File("C:\\Users\\TestGroup\\Desktop\\testing-app-automatizacion\\src\\main\\resources\\app-test-QA.apk").getAbsolutePath());//Cambiar ruta APK
        capabilities.setCapability("newCommandTimeout", 4000);
        
        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        mobiledriver = new AndroidDriver(url, capabilities);
    }
    @Test(dataProvider = "dataSet")
	public void login(String Username, String Password, String RepeatPassword, String Email) throws IOException, InterruptedException{
		// Registro Login
		mobiledriver.findElement(By.xpath("//android.widget.EditText[@text=\"Username\"]")).sendKeys(Username);
		mobiledriver.findElement(By.xpath("//android.widget.EditText[@text=\"Password\"]")).sendKeys(Password);
		mobiledriver.findElement(By.xpath("//android.widget.EditText[@text=\"Repeat password\"]")).sendKeys(RepeatPassword);
		mobiledriver.findElement(By.xpath("//android.widget.EditText[@text=\"Email (Optional)\"]")).sendKeys(Email);
		
		WebElement btn_Ingresar = mobiledriver.findElement(By.xpath("//android.widget.TextView[@text=\"SUBMIT\"]"));
		btn_Ingresar.click();
		
		WebElement btn_Regresar = mobiledriver.findElement(By.xpath("//android.widget.ImageView"));
		btn_Regresar.click();
		
	}
    
    @Test
    public void testUsernameValidation() throws InterruptedException, IOException {
        // i. No puede estar registrado anteriormente
        login("ionixtester", "ValidPass1!", "ValidPass1!", "");
        WebElement errorMessage = mobiledriver.findElement(By.xpath("//android.widget.EditText[@text=\"Username\"]"));
        Assert.assertTrue(errorMessage.getText().contains("Username already exists"));

        // ii. No debe permitir ingresar el caracter @
        login("user@name", "ValidPass1!", "ValidPass1!", "");
        Assert.assertTrue(errorMessage.getText().contains("Invalid character '@'"));

        // iii. No puede estar vacío
        login("", "ValidPass1!", "ValidPass1!", "");
        Assert.assertTrue(errorMessage.getText().contains("Username cannot be empty"));
    }

    @Test
    public void testPasswordValidation() throws InterruptedException, IOException {
        // i. Debe tener más de 8 caracteres de largo
    	login("newUser", "short", "short", "");
        WebElement errorMessage = mobiledriver.findElement(By.xpath("//android.widget.EditText[@text=\"Password\"]"));
        Assert.assertTrue(errorMessage.getText().contains("Password must be longer than 8 characters"));

        // ii. Debe contener una letra mayuscula
        login("newUser", "lowercasepassword", "lowercasepassword", "");
        Assert.assertTrue(errorMessage.getText().contains("Password must contain an uppercase letter"));

        // iii. Debe contener una letra minuscula
        login("newUser", "UPPERCASEPASSWORD", "UPPERCASEPASSWORD", "");
        Assert.assertTrue(errorMessage.getText().contains("Password must contain a lowercase letter"));

        // iv. Debe contener un caracter numerico
        login("newUser", "NoNumbersPassword", "NoNumbersPassword", "");
        Assert.assertTrue(errorMessage.getText().contains("Password must contain a numeric character"));

        // v. Debe contener un caracter especial
        login("newUser", "NoSpecialChar1", "NoSpecialChar1", "");
        Assert.assertTrue(errorMessage.getText().contains("Password must contain a special character"));

        // vi. No puede estar vacío
        login("newUser", "", "", "");
        Assert.assertTrue(errorMessage.getText().contains("Password cannot be empty"));
    }

    @Test
    public void testRepeatPasswordValidation() throws InterruptedException, IOException {
        // i. El dato ingresado debe ser idéntico al recuadro anterior
    	login("newUser", "ValidPass1!", "DifferentPass1!", "");
        WebElement errorMessage = mobiledriver.findElement(By.xpath("//android.widget.EditText[@text=\"Repeat password\"]"));
        Assert.assertTrue(errorMessage.getText().contains("Passwords do not match"));

        // ii. No puede estar vacío
        login("newUser", "ValidPass1!", "", "");
        Assert.assertTrue(errorMessage.getText().contains("Repeat password cannot be empty"));
    }

    @Test
    public void testEmailValidation() throws InterruptedException, IOException {
        // i. En caso de ser llenado, deberá tener el formato de email ( a@a.a )
    	login("newUser", "ValidPass1!", "ValidPass1!", "invalidEmail");
        WebElement errorMessage = mobiledriver.findElement(By.xpath("//android.widget.EditText[@text=\"Email (Optional)\"]"));
        Assert.assertTrue(errorMessage.getText().contains("Invalid email format"));
    }
    
    @AfterTest
    public void afterTest( ){
        mobiledriver.quit();
    }

    @DataProvider(name="dataSet")
    public Object[][] loginData() {
       // AutoTools = new Utils();
        Object[][] arrayObject = Utils.getExcelData("DataSet/dataSet.xls","Login");
        return arrayObject;
    }
}
