package com.sytner.user;

import commons.BaseTest;
import commons.GlobalConstants;
import commons.PageGeneratorManager;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.v85.page.Page;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.sytner.user.*;

@Feature("Menu items")
        public class User_01_Menu_Items extends BaseTest {

            @Parameters("browser")
            @BeforeClass
            public void beforeClass(String browserName) {
                log.info("Pre-conditions - Step 01: Open User Site");
                driver = getBrowserDriver(browserName, GlobalConstants.END_USER_URL);
                homePage = PageGeneratorManager.getUserHomePage(driver);

        log.info("Pre-conditions - Step 02: Click to 'Accept all cookie' option if show ");
        homePage.clickToAcceptAllCookiePopup();
    }

    @Description("TestMenu")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void Menu_01_About_Us() {
        homePage.openMenuHeader();
        homePage.clickDynamicMenuLinkByPageName("About Us");
        aboutUsPage = PageGeneratorManager.getUserAboutUsPage(driver);
        verifyEquals(aboutUsPage.getCurrentUrl(), "https://www.sytner.co.uk/about-us");
        verifyEquals(aboutUsPage.getCurrentTitlePage(), "About Sytner Group");
    }

    @Test
    public void Menu_02_Latest_News() {
        aboutUsPage.openPageUrl(GlobalConstants.END_USER_URL);
        homePage = PageGeneratorManager.getUserHomePage(driver);
        homePage.openMenuHeader();
        homePage.clickDynamicMenuLinkByPageName("Latest News");
        newsPage = PageGeneratorManager.getUserNewsPage(driver);
        verifyEquals(newsPage.getCurrentUrl(), "https://www.sytner.co.uk/news");
        verifyEquals(newsPage.getCurrentTitlePage(), "Latest News");
    }

    @Test
    public void Menu_03_Customer_Services() {
        newsPage.openPageUrl(GlobalConstants.END_USER_URL);
        homePage = PageGeneratorManager.getUserHomePage(driver);
        homePage.openMenuHeader();
        homePage.clickDynamicMenuLinkByPageName("Customer Services");
        customerServicePage = PageGeneratorManager.getUserCustomerServicePage(driver);
        verifyEquals(customerServicePage.getCurrentUrl(), "https://www.sytner.co.uk/customer-service");
        verifyEquals(customerServicePage.getCurrentTitlePage(), "Customer Service");
    }

    @Test
    public void Menu_04_Career() {
        customerServicePage.openPageUrl(GlobalConstants.END_USER_URL);
        homePage = PageGeneratorManager.getUserHomePage(driver);
        homePage.openMenuHeader();
        homePage.clickDynamicMenuLinkByPageName("Careers");
        careerPage = PageGeneratorManager.getUserCareerPage(driver);
        careerPage.clickToAcceptAllCookiePopup();
        verifyEquals(careerPage.getCurrentUrl(), "https://jobs.sytner.co.uk/jobs/");
    }


    @Test
    public void Menu_05_Finance() {
        careerPage.openPageUrl(GlobalConstants.END_USER_URL);
        homePage = PageGeneratorManager.getUserHomePage(driver);
        homePage.openMenuHeader();
        homePage.clickDynamicMenuLinkByPageName("Finance");
        financePage = PageGeneratorManager.getUserFinancePage(driver);
        verifyEquals(financePage.getCurrentUrl(), "https://www.sytner.co.uk/finance");
        verifyEquals(financePage.getCurrentTitlePage(), "Car Finance");

    }

    @Test
    public void Menu_06_Sytner_GroupLogo(){
        homePage = financePage.clickToLogoLinkAtHeaderNav();
        verifyEquals(homePage.getCurrentUrl(), "https://www.sytner.co.uk/");
    }


    @Test
    public void Menu_07_Search_Car_Page(){
        searchCarPage = homePage.clickToSearchButtonAtBanner();
        verifyEquals(searchCarPage.getCurrentUrl(), "https://www.sytner.co.uk/search");

    }







    @AfterClass(alwaysRun = true)
    public void afterClass() {
        closeBrowserDriver();
    }

    private WebDriver driver;
    private UserHomePO homePage;
    private UserAboutUsPO aboutUsPage;
    private UserNewsPO newsPage;
    private UserCustomerServicePO customerServicePage;
    private UserCareerPO careerPage;
    private UserFinancePO financePage;
    private UserSearchCarPO searchCarPage;


}
