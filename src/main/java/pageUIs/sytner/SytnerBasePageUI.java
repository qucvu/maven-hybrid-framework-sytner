package pageUIs.sytner;

public class SytnerBasePageUI {
    public final static String DYNAMIC_TEXTBOX_BY_ID = "css=input#%s";
    public final static String DYNAMIC_DEFAULT_DROPDOWN_BY_ID = "css=select#%s";
    public final static String DYNAMIC_FOOTER_LINK_PAGE_NAME = "xpath=//footer//div[contains(@class, 'ui-display-none')]//nav//a[text()='%s']";
    public final static String USE_COOKIES_POPUP = "ID=onetrust-banner-sdk";
    public final static String ACCEPT_ALL_COOKIES_BUTTON = "ID=onetrust-accept-btn-handler";
    public final static String OPEN_MENU_BUTTON = "CSS=button[title='Open Menu']";
    public final static String CLOSE_MENU_BUTTON = "CSS=button[title='Close']";
    public final static String DYNAMIC_MENU_LINK_BY_PAGE_NAME = "XPATH=//header//div[contains(@class, 'sui-display-none')]//a[text()='%s']";
    public final static String CURRENT_TITLE_PAGE = "css=div[class*=title-container]>h1";
    public final static String SYTNER_LOGO_LINK_HEADER_NAV = "css=a[id*=logo-link]";
    public final static String SEARCH_BUTTON_AT_BANNER = "css=button[id*=search-input]:not(button[id*='search-input-desktop-submit']):not(button[id*='search-input-mobile-submit'])";
}
