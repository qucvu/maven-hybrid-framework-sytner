package pageObjects.sytner.user;

import commons.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class UserLoginPO extends BasePage {
    public UserLoginPO(WebDriver driver) {
        super(driver);
    }

    @Step("Click to 'Login' button")
    public void clickToLoginButton(){
        sendKeyToElement("name=uid", "123");
        sleepInSecond(10);
    }


}
