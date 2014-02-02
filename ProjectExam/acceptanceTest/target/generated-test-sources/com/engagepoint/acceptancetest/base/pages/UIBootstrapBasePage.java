package com.engagepoint.acceptancetest.base.pages;

import net.thucydides.core.annotations.WhenPageOpens;
import net.thucydides.core.annotations.findby.FindBy;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UIBootstrapBasePage extends PageObject {

    @FindBy(css = ".ajax-loader-visible")
    private WebElement ajaxLoader;

    public UIBootstrapBasePage(WebDriver driver) {
        super(driver);
    }

    @WhenPageOpens
    public void waitForAjaxLoaderInvisible() {
        element(ajaxLoader).waitUntilNotVisible();
    }
}
