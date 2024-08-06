package driverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	public static WebDriver driver;
	String inputpath = "./FileInput/DataEngine.xlsx";
	String outputpath = "./FileOutput/HybridResults.xlsx";
	ExtentReports report;
	ExtentTest logger;

	public void startTest() throws Throwable {
		String ModuleStatus = " ";
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		String Testcases = "MaterTestCases";
		for (int i = 1; i <= xl.rowcount(Testcases); i++) {
			if (xl.getcelldata(Testcases, i, 2).equalsIgnoreCase("Y")) {
				String Tcmodule = xl.getcelldata(Testcases, i, 1);
				report = new ExtentReports("./target/Reports/" + Tcmodule + FunctionLibrary.generateDate() + ".html");
				logger = report.startTest(Tcmodule);
				for (int j = 1; j <= xl.rowcount(Tcmodule); j++) {
					String Description = xl.getcelldata(Tcmodule, j, 0);
					String Object_Type = xl.getcelldata(Tcmodule, j, 1);
					String Locator_type = xl.getcelldata(Tcmodule, j, 2);
					String locator_value = xl.getcelldata(Tcmodule, j, 3);
					String Test_Data = xl.getcelldata(Tcmodule, j, 4);
					try {
						if (Object_Type.equalsIgnoreCase("startBrowser")) {
							driver = FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("openUrl")) {
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("waitForElement")) {
							FunctionLibrary.waitForElement(Locator_type, locator_value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("typeAction")) {
							FunctionLibrary.typeAction(Locator_type, locator_value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("clickAction")) {
							FunctionLibrary.clickAction(Locator_type, locator_value);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("validateTitle")) {
							FunctionLibrary.validateTitle(Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("closeBrowser")) {
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("dropDownAction")) {
							FunctionLibrary.dropDownAction(Locator_type, locator_value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("captureStockNum")) {
							FunctionLibrary.captureStockNum(Locator_type, locator_value);
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("stockTable")) {
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("capturesup")) {
							FunctionLibrary.capturesup(Locator_type, locator_value);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("supplierTable")) {
							FunctionLibrary.supplierTable();
							logger.log(LogStatus.INFO, Description);
						}

						xl.setcelldata(Tcmodule, j, 5, "pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						ModuleStatus = "True";
					} catch (Exception e) {
						System.out.println(e.getMessage());
						xl.setcelldata(Tcmodule, j, 5, "fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						ModuleStatus = "False";
					}
					if (ModuleStatus.equalsIgnoreCase("True")) {
						xl.setcelldata(Testcases, i, 3, "pass", outputpath);
					} else {
						xl.setcelldata(Testcases, i, 3, "fail", outputpath);
					}
					report.endTest(logger);
					report.flush();
				}
			} else {
				xl.setcelldata(Testcases, i, 3, "blocked", outputpath);
			}
		}
	}
}
