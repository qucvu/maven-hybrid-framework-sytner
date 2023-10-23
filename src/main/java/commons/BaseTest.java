package commons;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import utilities.BrowserList;


public class BaseTest {
    private WebDriver driver;

    protected final Logger log;

    protected BaseTest() {
        log = LogManager.getLogger(getClass());
    }
    public WebDriver getDriverInstance() {
        return this.driver;
    }

    protected WebDriver getBrowserDriver(String browserName) {
        BrowserList browserList = BrowserList.valueOf(browserName.toUpperCase());
        switch (browserList) {
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setLogLevel(FirefoxDriverLogLevel.TRACE);
                firefoxOptions.setBinary(GlobalConstants.PROJECT_PATH + "\\browserLogs\\Firefox.log");
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case H_FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("-headless");
                options.addArguments("window-size=1920x1080");
                driver = new FirefoxDriver(options);
                break;
            case CHROME:
                WebDriverManager.chromedriver().setup();
                System.setProperty("webdriver.chrome.args", "--disable-logging");
                System.setProperty("webdriver.chrome.silentOutput", "true");
                File file = new File(GlobalConstants.PROJECT_PATH + "\\BrowserExtensions\\extension_2_0_13_0.crx");
                ChromeOptions optionsChrome = new ChromeOptions();
                optionsChrome.setAcceptInsecureCerts(true);
                optionsChrome.addExtensions(file);
                optionsChrome.addArguments("--lang=vi");
                optionsChrome.addArguments("--disable-notifications");
                optionsChrome.setExperimentalOption("useAutomationExtension", false);
                optionsChrome.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                driver = new ChromeDriver(optionsChrome);
                break;
            case SAFARI:
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
                break;
            case H_CHROME:
                WebDriverManager.chromedriver().setup();
                optionsChrome = new ChromeOptions();
                optionsChrome.addArguments("-headless");
                optionsChrome.addArguments("window-size=1920x1080");
                driver = new ChromeDriver(optionsChrome);
                break;
            case IE:
                WebDriverManager.iedriver().arch32().setup();
                driver = new InternetExplorerDriver();
                break;
            case EDGE:
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case COCCOC:
                WebDriverManager.chromedriver().driverVersion("109.0.5414.25").setup();
                ChromeOptions optionsCocCoc = new ChromeOptions();
                if (GlobalConstants.OS_NAME.contains("windows")) {
                    optionsCocCoc.setBinary("C:\\Program Files (x86)\\CocCoc\\Browser\\Application\\browser.exe");
                } else {
                    optionsCocCoc.setBinary("...");

                }
                driver = new ChromeDriver(optionsCocCoc);
                break;
            default:
                throw new RuntimeException("Browser name invalid");
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(GlobalConstants.LONG_TIMEOUT));
        return driver;
    }

    protected WebDriver getBrowserDriver(String browserName, String url) {
        switch (browserName) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxProfile profile = new FirefoxProfile();
                FirefoxOptions fireFoxoptions = new FirefoxOptions();
                fireFoxoptions.setProfile(profile);
                fireFoxoptions.setLogLevel(FirefoxDriverLogLevel.TRACE);
                fireFoxoptions.addArguments("--start-maximized");
//                fireFoxoptions.setBinary(GlobalConstants.PROJECT_PATH + "\\browserLogs\\Firefox.log");
                driver = new FirefoxDriver(fireFoxoptions);
                break;
            case "h_firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions hFirefoxOptions = new FirefoxOptions();
                hFirefoxOptions.addArguments("-headless");
                hFirefoxOptions.addArguments("window-size=1920x1080");
                driver  = new FirefoxDriver(hFirefoxOptions);
                break;
            case "chrome":
			    WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "h_chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions optionsChrome = new ChromeOptions();
                optionsChrome.addArguments("-headless");
                optionsChrome.addArguments("window-size=1920x1080");
                driver = new ChromeDriver(optionsChrome);
                break;
            case "ie":
                WebDriverManager.iedriver().arch32().setup();
                driver = new InternetExplorerDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "coccoc":
                WebDriverManager.chromedriver().driverVersion("109.0.5414.25").setup();
                ChromeOptions optionsCocCoc = new ChromeOptions();
                if (GlobalConstants.OS_NAME.contains("windows")) {
                    optionsCocCoc.setBinary("C:\\Program Files (x86)\\CocCoc\\Browser\\Application\\browser.exe");
                } else {
                    optionsCocCoc.setBinary("...");

                }
                driver = new ChromeDriver(optionsCocCoc);
                break;
            default:
                throw new RuntimeException("Browser name invalid");
        }
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(GlobalConstants.LONG_TIMEOUT));

        return driver;
    }

    protected int generateRandomNumber() {
        return new Random().nextInt(99999);
    }

    protected boolean verifyTrue(boolean condition) {
        boolean pass = true;
        try {
            Assert.assertTrue(condition);
            log.info(" -------------------------- PASSED -------------------------- ");

        } catch (Throwable e) {
            log.info(" -------------------------- FAILED -------------------------- ");
            pass = false;

            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    protected boolean verifyFalse(boolean condition) {
        boolean pass = true;
        try {
            Assert.assertFalse(condition);
            log.info(" -------------------------- PASSED -------------------------- ");
        } catch (Throwable e) {
            log.info(" -------------------------- FAILED -------------------------- ");
            pass = false;
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    protected boolean verifyEquals(Object actual, Object expected) {
        boolean pass = true;
        try {
            Assert.assertEquals(actual, expected);
            log.info(" -------------------------- PASSED -------------------------- ");
        } catch (Throwable e) {
            pass = false;
            log.info(" -------------------------- FAILED -------------------------- ");
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }


    protected void closeBrowserDriver() {
        String cmd = null;
        try {
            String osName = GlobalConstants.OS_NAME;
            log.info("OS name = " + osName);

            String driverInstanceName = driver.toString().toLowerCase();
            log.info("Driver instance name = " + driverInstanceName);

            String browserDriverName;

            if (driverInstanceName.contains("chrome")) {
                browserDriverName = "chromedriver";
            } else if (driverInstanceName.contains("internetexplorer")) {
                browserDriverName = "IEDriverServer";
            } else if (driverInstanceName.contains("firefox")) {
                browserDriverName = "geckodriver";
            } else if (driverInstanceName.contains("edge")) {
                browserDriverName = "msedgedriver";
            } else if (driverInstanceName.contains("opera")) {
                browserDriverName = "operadriver";
            } else {
                browserDriverName = "safaridriver";
            }

            if (osName.contains("Windows")) {
                // cmd = "taskkill /F /FI \"IMAGENAME eq " + browserDriverName + "*\"";
                cmd = "taskkill /f /im " + browserDriverName + ".exe /T";

            } else {
                cmd = "pkill " + browserDriverName;
            }

            if (driver != null) {
                driver.manage().deleteAllCookies();
                driver.quit();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            try {
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void deleteAllureResultFolder() {
        try {
            String pathFolderDownload = GlobalConstants.PROJECT_PATH + "allure-results";
            File file = new File(pathFolderDownload);
            File[] listOfFiles = file.listFiles();
            if (listOfFiles.length != 0) {
                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) {
                        new File(listOfFile.toString()).delete();
                    }
                }
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }


}
