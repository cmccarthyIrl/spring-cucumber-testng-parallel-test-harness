package com.cmccarthy.ui.page;

import com.cmccarthy.ui.annotations.PageObject;
import com.cmccarthy.ui.utils.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@PageObject
public class WikipediaCommonPage extends AbstractPage {

    @FindBy(how = How.CLASS_NAME, using = "mainpage-welcome-sitename")
    private WebElement CENTRAL_LOGO;

    public WikipediaCommonPage(DriverManager driverManager) {
        super(driverManager);
    }

    public boolean isPageOpened() throws NoSuchFieldException {
        return waitForElementPresent(CENTRAL_LOGO);
    }
}

