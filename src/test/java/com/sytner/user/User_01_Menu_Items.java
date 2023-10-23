package com.sytner.user;

import commons.BaseTest;
import commons.GlobalConstants;
import commons.PageGeneratorManager;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.sytner.user.UserLoginPO;

@Feature("New User")
public class User_01_Menu_Items extends BaseTest {

    @Parameters("browser")
    @BeforeClass
    public void beforeClass(String browserName) {
        log.info("Pre-conditions - Step 01: Open User Site");
        driver = getBrowserDriver(browserName, GlobalConstants.END_USER_URL);
        loginPage = PageGeneratorManager.getUserLoginPage(driver);

        log.info("Pre-conditions - Step 02: Sleep to Test");
        verifyEquals(1, 1);

    }

    @Description("TestMenu")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void Menu_01_About_Us() {
        loginPage.sleepInSecond(2);

    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        closeBrowserDriver();
    }

    private WebDriver driver;
    private UserLoginPO loginPage;

}
