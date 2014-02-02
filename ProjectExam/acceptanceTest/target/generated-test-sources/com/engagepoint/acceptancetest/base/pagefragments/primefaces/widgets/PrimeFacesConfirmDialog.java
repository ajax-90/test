package com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.engagepoint.acceptancetest.base.pagefragments.ModalWidget;

import net.thucydides.core.annotations.findby.By;
import net.thucydides.core.pages.WebElementFacadeImpl;

public class PrimeFacesConfirmDialog extends WebElementFacadeImpl implements ModalWidget {

	private WebElement root;

	public PrimeFacesConfirmDialog(WebDriver driver, WebElement webElement,
			long timeoutInMilliseconds) {
		super(driver, webElement, timeoutInMilliseconds);
		this.root = webElement;
	}

	@Override
	public void close() {
		waitUntilVisible();
		root.findElement(By.cssSelector("[class*='close']")).click();
		waitUntilNotVisible();
	}

	@Override
	public void clickOn(String buttonText) {
		waitUntilVisible();
		root.findElement(By.xpath(".//button[contains(.,'" + buttonText + "')]")).click();
		waitUntilNotVisible();
	}

	@Override
	public String getContentText() {
		return root.findElement(By.xpath(".//*[contains(@class, 'content') or contains(@class, 'body')]")).getText();
	}

}
