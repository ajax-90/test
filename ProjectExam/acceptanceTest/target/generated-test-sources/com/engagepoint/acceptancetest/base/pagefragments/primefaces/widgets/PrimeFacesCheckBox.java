package com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.engagepoint.acceptancetest.base.pagefragments.CheckBoxWidget;

import net.thucydides.core.pages.WebElementFacadeImpl;

public class PrimeFacesCheckBox extends WebElementFacadeImpl implements CheckBoxWidget {

	private WebElement root;

	public PrimeFacesCheckBox(WebDriver driver, WebElement webElement,
			long timeoutInMilliseconds) {
		super(driver, webElement, timeoutInMilliseconds);
		this.root = webElement;
	}

	public boolean isChecked() {
		WebElement checkBoxIcon = root.findElement(By.cssSelector(".ui-chkbox-icon"));
		return checkBoxIcon.getAttribute("class").contains("ui-icon-check");
	}
	
	public void click(){
		root.findElement(By.cssSelector(".ui-chkbox-box")).click();
	}
}
