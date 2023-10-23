package commons;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageUIs.sytner.BankGuruBasePageUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BasePage {

    private final long longTimeout = GlobalConstants.LONG_TIMEOUT;
    private final long shortTimeout = GlobalConstants.SHORT_TIMEOUT;
    protected WebDriver driver;

    public static BasePage getBasePageObject() {
        return new BasePage();
    }

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public BasePage() {
    }


    public void openPageUrl(String pageUrl) {
        driver.get(pageUrl);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public Set<Cookie> getAllCookies() {
        return driver.manage().getCookies();
    }

    public void setCookies(Set<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            driver.manage().addCookie(cookie);
        }
        sleepInSecond(1);
    }

    public void sleepInSecond(long timeInSecond) {
        try {
            Thread.sleep(timeInSecond * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getPageSource() {
        return driver.getPageSource();
    }

    public void backToPage() {
        driver.navigate().back();
    }

    public void refreshCurrentPage() {
        driver.navigate().refresh();
    }

    protected Alert waitForAlertPresence() {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        return explicitWait.until(ExpectedConditions.alertIsPresent());
    }

    protected boolean isAlertPresent(WebDriver driver) {
        boolean presentFlag = false;
        try {
            driver.switchTo().alert();
            presentFlag = true;
        } catch (NoAlertPresentException ex) {
            ex.printStackTrace();
        }

        return presentFlag;
    }

    public void acceptAlert() {
        waitForAlertPresence().accept();
    }

    protected void cancelAlert() {
        waitForAlertPresence().dismiss();
    }

    protected String getAlertText() {
        return waitForAlertPresence().getText();
    }

    protected void sendKeyToAlert(String textValue) {
        waitForAlertPresence().sendKeys(textValue);
    }

    protected String getWindowHandle(WebDriver driver) {
        return driver.getWindowHandle();
    }

    protected void switchToWindowById(String windowId) {
        Set<String> allWindowIds = driver.getWindowHandles();
        for (String id : allWindowIds) {
            if (!id.equals(windowId)) {
                driver.switchTo().window(id);
            }
        }
    }

    protected void switchToWindowByTitle(String expectedTitlePage) {
        Set<String> allWindowIds = driver.getWindowHandles();
        for (String id : allWindowIds) {
            driver.switchTo().window(id);
            String actualTitle = driver.getTitle();
            if (actualTitle.equals(expectedTitlePage)) {
                break;
            }
        }
    }

    protected void closeOtherTabsWithoutParent(WebDriver driver, String parentId) {
        Set<String> allIds = driver.getWindowHandles();
        for (String id : allIds) {
            if (!id.equals(parentId)) {
                driver.switchTo().window(id).close();
            }
        }
        driver.switchTo().window(parentId);
    }

    private By getByLocator(String locatorType) {
        By by;
        if (locatorType.startsWith("id=") || locatorType.startsWith("ID=") || locatorType.startsWith("Id=")) {
            by = By.id(locatorType.substring(3));
        } else if (locatorType.startsWith("class=") || locatorType.startsWith("CLASS=") || locatorType.startsWith("Class=")) {
            by = By.className(locatorType.substring(6));
        } else if (locatorType.startsWith("name=") || locatorType.startsWith("NAME=") || locatorType.startsWith("Name=")) {
            by = By.name(locatorType.substring(5));

        } else if (locatorType.startsWith("css=") || locatorType.startsWith("CSS=") || locatorType.startsWith("Css=")) {
            by = By.cssSelector(locatorType.substring(4));

        } else if (locatorType.startsWith("xpath=") || locatorType.startsWith("XPATH=") || locatorType.startsWith("XPath=") || locatorType.startsWith("Xpath=")) {
            by = By.xpath(locatorType.substring(6));

        } else {
            throw new RuntimeException("The locator type is not supported!");
        }
        return by;
    }

    private String getDynamicXpath(String locatorType, String... dynamicValues) {
        // if (locatorType.startsWith("xpath=") || locatorType.startsWith("XPATH=") || locatorType.startsWith("XPath=") || locatorType.startsWith("Xpath=")) {
        // locatorType = String.format(locatorType, dynamicValues);
        // } else {
        // throw new RuntimeException("The locator type is only supported for the XPath locator");
        // }
        locatorType = String.format(locatorType, (Object[]) dynamicValues);
        return locatorType;

    }


    public WebElement getWebElement(String locatorType) {
        if (driver.toString().contains("internet explorer")) {
            sleepInSecond(3);
        }
        return driver.findElement(getByLocator(locatorType));
    }

    private WebElement getWebElement(String locatorType, String... dynamicValues) {
        return driver.findElement(getByLocator(getDynamicXpath(locatorType, dynamicValues)));
    }

    public List<WebElement> getListElements(String locatorType) {
        return driver.findElements(getByLocator(locatorType));
    }

    protected List<WebElement> getListElements(String locatorType, String... dynamicValues) {
        return driver.findElements(getByLocator(getDynamicXpath(locatorType, dynamicValues)));
    }

    protected void clickToElement(String locatorType) {
        if (driver.toString().contains("internet explorer")) {
            sleepInSecond(3);
            clickToElementByJS(locatorType);
        } else {
            getWebElement(locatorType).click();
        }

    }

    protected void clickToElementByJS(String locatorType) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getWebElement(locatorType));
    }


    protected void clickToElement(String locatorType, String... dynamicValues) {
        this.getWebElement(locatorType, dynamicValues).click();
    }

    protected void sendKeyToElement(String locatorType, String textValue) {
        WebElement element = getWebElement(locatorType);
        element.clear();
        element.sendKeys(textValue);
    }

    protected void sendKeyToElement(String locatorType, String textValue, String... dynamicValues) {
        WebElement element = getWebElement(locatorType, dynamicValues);
        element.clear();
        element.sendKeys(textValue);
    }


    protected void clearElement(String locatorType) {
        WebElement element = getWebElement(locatorType);
        element.clear();
    }

    protected void clearElement(String locatorType, String... dynamicValues) {
        WebElement element = getWebElement(locatorType, dynamicValues);
        element.clear();
    }


    public String getElementText(String locatorType) {
        return getWebElement(locatorType).getText();
    }

    protected String getElementText(String locatorType, String... dynamicValues) {
        return getWebElement(locatorType, dynamicValues).getText();
    }

    protected void selectItemInDefaultDropdown(String locatorType, String textItem) {
        Select select = new Select(getWebElement(locatorType));
        select.selectByVisibleText(textItem);
    }

    protected void selectItemInDefaultDropdown(String locatorType, String textItem, String... dynamicValues) {
        Select select = new Select(getWebElement(locatorType, dynamicValues));
        select.selectByVisibleText(textItem);
    }

    protected String getSelectedItemDefaultDropdown(String locatorType) {
        Select select = new Select(getWebElement(locatorType));
        return select.getFirstSelectedOption().getText();
    }

    protected String getSelectedItemDefaultDropdown(String locatorType, String... dynamicValues) {
        Select select = new Select(getWebElement(locatorType, dynamicValues));
        return select.getFirstSelectedOption().getText();
    }

    protected boolean isDropdownMultiple(String locatorType) {
        Select select = new Select(getWebElement(locatorType));
        return select.isMultiple();
    }

    protected void selectItemDropdown(String parentXpath, String allItemXpath, String expectedTextItem) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        getWebElement(parentXpath).click();
        sleepInSecond(1);
        List<WebElement> speedDropdownItems = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByLocator(allItemXpath)));
        for (WebElement webElement : speedDropdownItems) {
            if (webElement.getText().trim().equals(expectedTextItem)) {
                webElement.click();
                break;
            }
        }
    }

    protected void enterAndSelectItemDropdown(String textBoxXpath, String allItemXpath, String expectedTextItem) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement element = getWebElement(textBoxXpath);
        element.clear();
        element.sendKeys(expectedTextItem);
        sleepInSecond(1);
        List<WebElement> speedDropdownItems = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByLocator(allItemXpath)));
        for (WebElement webElement : speedDropdownItems) {
            if (webElement.getText().trim().equals(expectedTextItem)) {
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
                webElement.click();
                break;
            }
        }
    }

    protected String getElementAttribute(String attributeName, String locatorType) {
        return getWebElement(locatorType).getAttribute(attributeName);
    }

    protected String getElementAttribute(String attributeName, String locatorType, String... dynamicValues) {
        return getWebElement(getDynamicXpath(locatorType, dynamicValues)).getAttribute(attributeName);
    }

    protected String getElementCssValue(String locatorType, String propertyName) {
        return getWebElement(locatorType).getCssValue(propertyName);
    }

    protected String getHexColorFromRGBA(String rgbaValue) {
        return Color.fromString(rgbaValue).asHex();
    }

    protected int getElementsSize(String locatorType) {
        return getListElements(locatorType).size();
    }

    protected int getElementsSize(String locatorType, String... dynamicValues) {
        return getListElements(locatorType, dynamicValues).size();
    }

    protected void checkToDefaultCheckboxRadio(String locatorType) {
        WebElement element = getWebElement(locatorType);
        if (!element.isSelected()) {
            element.click();
        }
    }

    protected void checkToDefaultCheckboxRadio(String locatorType, String... dynamicValues) {
        WebElement element = getWebElement(locatorType, dynamicValues);
        if (!element.isSelected()) {
            element.click();
        }
    }

    protected void unCheckToDefaultCheckbox(String locatorType) {
        WebElement element = getWebElement(locatorType);
        if (element.isSelected()) {
            element.click();
        }
    }

    protected void unCheckToDefaultCheckbox(String locatorType, String... dynamicValues) {
        WebElement element = getWebElement(locatorType, dynamicValues);
        if (element.isSelected()) {
            element.click();
        }
    }

    protected boolean isElementDisplayed(String locatorType) {
        try {
            // displayed - có trong dom: true
            // undisplayed - có trong dom: trả về false
            return getWebElement(locatorType).isDisplayed();
        } catch (NoSuchElementException e) {
            // undisplayed: k có trong dom --> Quá lâu
            return false;
        }
    }

    protected boolean isElementDisplayed(String locatorType, String... dynamicValues) {
        return getWebElement(locatorType, dynamicValues).isDisplayed();
    }

    protected boolean isElementUndisplayed(String locatorType) {
        overrideImplicitTimeout(shortTimeout);
        List<WebElement> elements = getListElements(locatorType);
        overrideImplicitTimeout(longTimeout);

        if (elements.size() == 0)
            return true;
        else if (!elements.get(0).isDisplayed())
            return true;
        return false;
    }

    protected boolean isElementUndisplayed(String locatorType, String... dynamicValues) {
        overrideImplicitTimeout(shortTimeout);
        List<WebElement> elements = getListElements(locatorType, dynamicValues);
        overrideImplicitTimeout(longTimeout);

        if (elements.size() == 0)
            return true;
        else return !elements.get(0).isDisplayed();
    }

    protected boolean isElementEnabled(String locatorType) {
        return getWebElement(locatorType).isEnabled();
    }

    protected boolean isElementSelected(String locatorType) {
        return getWebElement(locatorType).isSelected();
    }

    protected boolean isElementSelected(String locatorType, String... dynamicValues) {
        return getWebElement(getDynamicXpath(locatorType, dynamicValues)).isSelected();
    }

    protected void switchToFrameIframe(String locatorType) {
        driver.switchTo().frame(getWebElement(locatorType));
    }

    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    protected void hoverMouseToElement(String locatorType) {
        WebElement element = getWebElement(locatorType);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    protected void hoverMouseToElement(String locatorType, String... dynamicValues) {
        WebElement element = getWebElement(getDynamicXpath(locatorType, dynamicValues));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    protected void pressKeyToElement(String locatorType, Keys key) {
        Actions actions = new Actions(driver);
        actions.sendKeys(getWebElement(locatorType), key).build().perform();
    }

    public void pressKeyToElement(String locatorType, Keys key, String... dynamicValues) {
        Actions actions = new Actions(driver);
        actions.sendKeys(getWebElement(getDynamicXpath(locatorType, dynamicValues)), key).build().perform();
    }

    public void scrollToBottomPage() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    protected void highlightElement(String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement element = getWebElement(locatorType);
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    protected void highlightElement(String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement element = getWebElement(getDynamicXpath(locatorType, dynamicValues));
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }


    protected void scrollToElement(String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(locatorType));
    }

    protected void scrollToElement(String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(getDynamicXpath(locatorType, dynamicValues)));
    }

    protected String getElementValueByJS(String xpathLocator) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        xpathLocator = xpathLocator.substring(6);
        return (String) jsExecutor.executeScript("$(document.evaluate(" + xpathLocator + ", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;).val");
    }

    protected void removeAttributeInDOM(String locatorType, String attributeRemove) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", getWebElement(locatorType));
    }

    protected boolean areJQueryAndJSLoadedSuccess() {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) jsExecutor.executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
            }
        };

        return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
    }

    protected WebElement getShadowDom(String locatorType) {
        return (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", getWebElement(locatorType));
    }

    protected String getElementValidationMessage(String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", getWebElement(locatorType));
    }

    protected boolean isImageLoaded(String locator) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
                getWebElement(locator));
        return status;
    }

    protected boolean isImageLoaded(String locator, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
                getWebElement(getDynamicXpath(locator, dynamicValues)));
    }

    private void overrideImplicitTimeout(long timeOut) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeOut));
    }

    protected void waitForElementVisibility(String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locatorType)));
    }

    protected void waitForElementVisibility(String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }

    protected void waitForElementInvisibility(String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locatorType)));

    }

    protected void waitForElementInvisibility(String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }

    protected void waitForElementUnDisplayed(String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
        overrideImplicitTimeout(shortTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locatorType)));
        overrideImplicitTimeout(longTimeout);

    }

    protected void waitForElementUnDisplayed(String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
        overrideImplicitTimeout(shortTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
        overrideImplicitTimeout(longTimeout);
    }

    protected void waitForAllElementVisibility(String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(locatorType)));

    }

    protected void waitForAllElementVisibility(String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(getDynamicXpath(locatorType, dynamicValues))));

    }

    protected void waitForAllElementInVisibility(String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListElements(locatorType)));
    }

    protected void waitForAllElementInVisibility(String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListElements(locatorType, dynamicValues)));
    }

    protected void waitForElementClickable(String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByLocator(locatorType)));
    }

    protected void waitForElementClickable(String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }

    protected void waitForElementStaleness(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.stalenessOf(getWebElement(locatorType)));
    }

    public void uploadFilesByFileName(String... fileNames) {
        String filePath = GlobalConstants.UPLOAD_FILE_FOLDER;
        String fullName = "";
        for (String fileName : fileNames) {
            fullName += filePath + fileName + "\n";
        }
        fullName = fullName.trim();
//        getWebElement(driver, HomePageUI.UPLOAD_FILE).sendKeys(fullName);

    }

    public boolean isDataStringSortAscLamDa(String locator) {
        List<WebElement> elementList = getListElements(locator);
        List<String> dataList = elementList.stream().map(n -> n.getText()).collect(Collectors.toList());
        List<String> sortList = new ArrayList<String>(dataList);
        Collections.sort(sortList);
        return sortList.equals(dataList);
    }

    public boolean isDataStringSortDescLamDa(String locator) {
        List<WebElement> elementList = getListElements(locator);
        List<String> dataList = elementList.stream().map(n -> n.getText()).collect(Collectors.toList());
        List<String> sortList = new ArrayList<String>(dataList);
        Collections.sort(sortList);
        Collections.reverse(sortList);
        return sortList.equals(dataList);
    }

    /*
     * @param textID the ID of textbox
     * @param value  text value
     */
    public void inputToTextboxById(String textID, String value) {
        waitForElementVisibility(BankGuruBasePageUI.DYNAMIC_TEXTBOX_BY_ID, textID);
        sendKeyToElement(BankGuruBasePageUI.DYNAMIC_TEXTBOX_BY_ID, value, textID);
    }

    protected void deleteFileByFilePath(String filePath) {
        try {
            Files.deleteIfExists(
                    Paths.get(filePath));
        } catch (NoSuchFileException e) {
            System.out.println(
                    "No such file/directory exists");
        } catch (DirectoryNotEmptyException e) {
            System.out.println("Directory is not empty.");
        } catch (IOException e) {
            System.out.println("Invalid permissions.");
        }

    }

    protected void writeDataIntoDataRecordByFileName(String data, String fileName) {
        File file = new File(GlobalConstants.DATA_RECORD + fileName + ".txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file, true);
            fr.write(data);
            fr.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fr != null;
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean waitForFileToDownload(String expectedFullPathName, int maxWaitSeconds) {
        File downloadedFile = new File(expectedFullPathName);
        System.out.println("Download file: " + downloadedFile);
        long startTime = System.currentTimeMillis();

        while (!downloadedFile.exists()) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = TimeUnit.MILLISECONDS.toSeconds(currentTime - startTime);
            if (elapsedTime > maxWaitSeconds) {
                return false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    public String getValueTextboxById(String id) {
        waitForElementVisibility(BankGuruBasePageUI.DYNAMIC_TEXTBOX_BY_ID, id);
        return getElementAttribute("value", BankGuruBasePageUI.DYNAMIC_TEXTBOX_BY_ID, id);
    }

    public void enterToTextboxEmptyValueById(String id) {
        waitForAllElementVisibility(BankGuruBasePageUI.DYNAMIC_TEXTBOX_BY_ID, id);
        clearElement(BankGuruBasePageUI.DYNAMIC_TEXTBOX_BY_ID, id);
    }

    public void selectToDefaultDropdownById(String Id, String textOption) {
        waitForElementClickable(BankGuruBasePageUI.DYNAMIC_DEFAULT_DROPDOWN_BY_ID, Id);
        selectItemInDefaultDropdown(BankGuruBasePageUI.DYNAMIC_DEFAULT_DROPDOWN_BY_ID, textOption, Id);
    }


}
