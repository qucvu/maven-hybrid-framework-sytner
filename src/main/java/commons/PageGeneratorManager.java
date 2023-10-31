package commons;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pageObjects.sytner.user.*;

public class PageGeneratorManager {

    @Step("Initialize User Home Page")
    public static UserHomePO getUserHomePage(WebDriver driver){
        return new UserHomePO(driver);
    }
    @Step("Initialize User About us Page")
    public static UserAboutUsPO getUserAboutUsPage(WebDriver driver){
        return new UserAboutUsPO(driver);
    }
    @Step("Initialize User News Page")
    public static UserNewsPO getUserNewsPage(WebDriver driver){
        return new UserNewsPO(driver);
    }

    @Step("Initialize User Customer Service Page")
    public static UserCustomerServicePO getUserCustomerServicePage(WebDriver driver){
        return new UserCustomerServicePO(driver);
    }

    @Step("Initialize User Career Page")
    public static UserCareerPO getUserCareerPage(WebDriver driver){
        return new UserCareerPO(driver);
    }

    @Step("Initialize User Finance Page")
    public static UserFinancePO getUserFinancePage(WebDriver driver){
        return new UserFinancePO(driver);
    }



}
