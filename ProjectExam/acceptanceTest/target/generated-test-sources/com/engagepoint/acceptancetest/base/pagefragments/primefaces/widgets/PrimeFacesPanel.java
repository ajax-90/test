package com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets;

import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.escapeJSId;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.engagepoint.acceptancetest.base.pagefragments.PanelWidget;
import com.engagepoint.acceptancetest.base.webelements.utils.ExtendedExpectedConditions;

import net.thucydides.core.pages.WebElementFacadeImpl;

public class PrimeFacesPanel extends WebElementFacadeImpl implements PanelWidget {

	private static final By OPTION_MENU_CSS_SELECTOR = By.cssSelector("[role='menu']");
	private WebElement root;
	private WebDriver driver;

	public PrimeFacesPanel(WebDriver driver, WebElement webElement,
			long timeoutInMilliseconds) {
		super(driver, webElement, timeoutInMilliseconds);
		this.root = webElement;
		this.driver = driver;
	}

	public void close() {
		root.findElement(By.cssSelector("[id$='_closer']")).click();
		waitUntilPanelAnimationsComplete();
	}

	public void toggle() {
		root.findElement(By.cssSelector("[id$='_toggler']")).click();
		waitUntilPanelAnimationsComplete();
	}

	public void chooseOption(String option) {
		root.findElement(By.cssSelector("[id$='_menu']")).click();
		waitUntil(ExpectedConditions.visibilityOfElementLocated(OPTION_MENU_CSS_SELECTOR));
		WebElement menu = driver.findElement(OPTION_MENU_CSS_SELECTOR);
		menu.findElement(By.xpath(".//*[@class='ui-menuitem-text' and text()='"+ option +"']")).click();
		waitUntil(ExpectedConditions.invisibilityOfElementLocated(OPTION_MENU_CSS_SELECTOR));
		waitUntilPanelAnimationsComplete();
	}

	private <T> void waitUntil(ExpectedCondition<T> expectedCondition) {
		waitForCondition().until(expectedCondition);
	}

	private void waitUntilPanelAnimationsComplete() {
		waitUntil(ExtendedExpectedConditions.animationCompletes(escapeJSId(root.getAttribute("id")) + " *"));
	}

	public boolean isContentVisible() {
		waitUntilPanelAnimationsComplete();
		return root.findElement(By.cssSelector("[id$='_content']")).isDisplayed();
	}
}
