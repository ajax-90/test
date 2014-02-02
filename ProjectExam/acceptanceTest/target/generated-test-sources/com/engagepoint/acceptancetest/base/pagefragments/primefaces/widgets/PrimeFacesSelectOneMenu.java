package com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.engagepoint.acceptancetest.base.pagefragments.SelectOneMenuWidget;

import net.thucydides.core.pages.WebElementFacadeImpl;

public class PrimeFacesSelectOneMenu extends WebElementFacadeImpl implements SelectOneMenuWidget{

	private WebElement root;
	private WebDriver driver;

	public PrimeFacesSelectOneMenu(WebDriver driver, WebElement webElement,
			long timeoutInMilliseconds) {
		super(driver, webElement, timeoutInMilliseconds);
		this.root = webElement;
		this.driver = driver;
	}

	public void select(String option) {
		root.findElement(By.tagName("label")).click();
		WebElement selectonemenuPanel = driver.findElement(By.id(root.getAttribute("id") + "_panel"));
		waitForCondition().until(ExpectedConditions.visibilityOf(selectonemenuPanel));
        WebElement selectonemenuPanelElement = selectonemenuPanel.findElement(By.xpath(".//li[text()='" + option + "']"));
        getJavascriptExecutorFacade().executeScript("arguments[0].scrollIntoView();", selectonemenuPanelElement);
        selectonemenuPanelElement.click();
	}

	public String getSelected() {
		return root.findElement(By.tagName("label")).getText();
	}

}
