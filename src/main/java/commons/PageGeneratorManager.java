package commons;

import org.openqa.selenium.WebDriver;
import pageObjects.sytner.user.UserLoginPO;

public class PageGeneratorManager {
    public static UserLoginPO getUserLoginPage(WebDriver driver){
        return new UserLoginPO(driver);
    }
}
