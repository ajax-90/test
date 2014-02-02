package com.engagepoint.acceptancetest.base.pages;

import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.getFirstVisibleElementFrom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import net.thucydides.core.pages.PageObject;

public abstract class AbstractPage extends PageObject {

	public AbstractPage(WebDriver driver) {
		super(driver);
	}

	protected boolean isEnabledByClass(WebElement element) {
		return !element.getAttribute("class").contains("ui-state-disabled");
	}

	protected WebElement getRadioButtonByLabel(String labelText) {
		WebDriver driver = getDriver();
		String forAttribute = driver.findElement(By.xpath("//label[text()='" + labelText + "']")).getAttribute("for");
		return driver.findElement(By.xpath("//*[@for='" + forAttribute + "']"));
	}

	protected WebElement getVisibleButtonWithText(String text) {
		return getFirstVisibleElementFrom(getDriver().findElements(By.xpath("//button[contains(.,'" + text + "')]")));
	}

	protected WebElement getVisibleLinkWithText(String text) {
		return getFirstVisibleElementFrom(getDriver().findElements(By.xpath("//a[contains(.,'" + text + "')]")));
	}

	protected void clickOnLinkInComponentByText(String menuItem, WebElement component) {
		component.findElement(By.xpath("//a[contains(.,'" + menuItem + "')]")).click();
	}

}
