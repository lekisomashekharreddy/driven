package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;



public class FunctionLibrary 
{
	public static Properties conpro;
	public static WebDriver driver;
	public static WebDriver startBrowser()throws Throwable
	{
		conpro = new Properties();
		conpro.load(new FileInputStream("PropertyFiles/Enviornment.properties"));
		if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();
		}
		else
		{
			Reporter.log("Browser value is not matching",true);
		}
		return driver;
	}
	public static void openUrl()
	{
		driver.get(conpro.getProperty("Url"));
	}
	public static void waitForElement(String Locator_Type , String Locator_Value , String Test_Data)
	{
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(Test_Data)));
		if(Locator_Type.equalsIgnoreCase("xpath"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locator_Value)));	
		}
		if(Locator_Type.equalsIgnoreCase("id"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(Locator_Value)));	
		}
		if(Locator_Type.equalsIgnoreCase("name"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(Locator_Value)));	
		}
	}
	public static void typeAction(String Locator_Type , String Locator_Value , String Test_Data)
	{
		if (Locator_Type.equalsIgnoreCase("xpath"))	
		{
			driver.findElement(By.xpath(Locator_Value)).clear();
			driver.findElement(By.xpath(Locator_Value)).sendKeys(Test_Data);
		}
		if(Locator_Type.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(Locator_Value)).clear();
			driver.findElement(By.id(Locator_Value)).sendKeys(Test_Data);
		}
		if(Locator_Type.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(Locator_Value)).clear();
			driver.findElement(By.name(Locator_Value)).sendKeys(Test_Data);
		}
	}
	public static void clickAction(String Locator_type , String Locator_Value )
	{
	if (Locator_type.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(Locator_Value)).click();
	}
	if(Locator_type.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(Locator_Value)).sendKeys(Keys.ENTER);
	}
	if(Locator_type.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(Locator_Value)).click();
	}
	}
	public static void validateTitle(String Expected_Title)
	{
		String Actual_Title = driver.getTitle();
		try {
	Assert.assertEquals(Actual_Title, Expected_Title , "Title is Not Matching");
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());
		}
	}
	public static void closeBrowser()
	{
		driver.quit();
	}
	public static String generateDate()
	{
		Date date = new Date ();
		DateFormat df = new SimpleDateFormat("YYYY_MM_dd");
		return df.format(date);
		}
	public static void dropDownAction(String Locator_Type , String Locator_Value , String Test_Data)
	{
		if(Locator_Type.equalsIgnoreCase("xpath"))
		{
			int value = Integer.parseInt(Test_Data);
			Select element = new Select(driver.findElement(By.xpath(Locator_Value)));
			element.selectByIndex(value);
		}
		if(Locator_Type.equalsIgnoreCase("name"))
		{
			int value = Integer.parseInt(Test_Data);
			Select element = new Select(driver.findElement(By.name(Locator_Value)));
			element.selectByIndex(value);
		}
		if(Locator_Type.equalsIgnoreCase("id"))
		{
			int value = Integer.parseInt(Test_Data);
			Select element = new Select(driver.findElement(By.id(Locator_Value)));
			element.selectByIndex(value);
		}
	}
	public static void captureStockNum(String Locator_Type , String Locator_Value) throws Throwable
	{
		String StockNum = "";
		if(Locator_Type.equalsIgnoreCase("id"))
		{
			StockNum = driver.findElement(By.id(Locator_Value)).getAttribute("value");
		}
		if(Locator_Type.equalsIgnoreCase("name"))
		{
			StockNum = driver.findElement(By.name(Locator_Value)).getAttribute("value");
		}
		if(Locator_Type.equalsIgnoreCase("xpath"))
		{
			StockNum = driver.findElement(By.xpath(Locator_Value)).getAttribute("value");
		}
		FileWriter fw = new FileWriter("./CaptureData/stocknumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(StockNum);
		bw.flush();
		bw.close();
		
	}
	public static void stockTable()throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/stocknumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data = br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).isDisplayed());
		driver.findElement(By.xpath(conpro.getProperty("Search-Panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("Search-Button"))).click();
		Thread.sleep(3000);
		String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log(Exp_Data+"   "+Act_Data , true);
		try {
			Assert.assertEquals(Exp_Data, Act_Data , "Stock number not matching");
		}catch(AssertionError e)
		{
			System.out.println(e.getMessage());
		}
	}
	
public static void capturesup(String Locator_Type , String Locator_Value)throws Throwable
{
String SupplierNum ="";
if(Locator_Type.equalsIgnoreCase("xpath"))
{
	SupplierNum = driver.findElement(By.xpath(Locator_Value)).getAttribute("value");
}
if(Locator_Type.equalsIgnoreCase("id"))
{
	SupplierNum = driver.findElement(By.id(Locator_Value)).getAttribute("value");
}
if(Locator_Type.equalsIgnoreCase("name"))
{
	SupplierNum = driver.findElement(By.name(Locator_Value)).getAttribute("value");
}
FileWriter fw = new FileWriter("./CaptureData/Suppliernumber.txt");
BufferedWriter bw = new BufferedWriter(fw);
bw.write(SupplierNum);
bw.flush();
bw.close();
}
public static void supplierTable()throws Throwable
{
	FileReader fr = new FileReader("./CaptureData/Suppliernumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("Search-Panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("Search-Textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("Search-Button"))).click();
	Thread.sleep(2000);
	String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
	Reporter.log(Exp_Data+"  "+ Act_Data,true);
	try {
		Assert.assertEquals(Exp_Data, Act_Data , "supplier number not matching");
	}catch(AssertionError a)
	{
		System.out.println(a.getMessage());
	}
}

}








