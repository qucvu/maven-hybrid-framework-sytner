package reportConfigs;

import commons.GlobalConstants;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import commons.BaseTest;
import io.qameta.allure.Attachment;

import java.io.File;

public class AllureTestListener implements ITestListener {

	// Screenshot attachments for Allure
	@Attachment(value = "Screenshot of {0}", type = "image/png")
	public static void saveScreenshotPNG(String testName, WebDriver driver) {
		((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		Object testClass = iTestResult.getInstance();
		WebDriver driver = ((BaseTest) testClass).getDriverInstance();
		saveScreenshotPNG(iTestResult.getName(), driver);
	}

	@Override
	public void onStart(ITestContext iTestContext) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFinish(ITestContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestStart(ITestResult arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		// TODO Auto-generated method stub
	}

}
