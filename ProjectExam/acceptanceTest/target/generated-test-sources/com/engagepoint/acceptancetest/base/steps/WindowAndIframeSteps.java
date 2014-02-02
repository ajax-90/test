package com.engagepoint.acceptancetest.base.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

public class WindowAndIframeSteps extends ScenarioSteps{

	private static final long serialVersionUID = 1L;

	private static final String IFRAME_TAG = "iframe";
	
	@Steps
	private JbehaveBaseSteps jbehaveBase;

	public WindowAndIframeSteps(Pages pages) {
		super(pages);
	}
	
	@When("the user fills '$id' editor with '$value'")
	public void whenTheUserFillsEditorWith(String id, String value) {
		givenIframe(id);
		getDriver().findElement(By.tagName("body")).sendKeys(value);
	}
	
	@Given("'$id' iframe")
	public void givenIframe(String id) {
		By editorBy = jbehaveBase.findVisibleElementAndGetSelector(id);
		WebDriver driver = getDriver();
		WebElement editorElement = driver.findElement(editorBy);
		if(!editorElement.getTagName().contentEquals(IFRAME_TAG))
			editorElement = editorElement.findElement(By.tagName(IFRAME_TAG));
		driver.switchTo().frame(editorElement);
	}
	
	@When("return to page from iframe")
	public void whenReturnToPageFromIframe() {
		getDriver().switchTo().defaultContent();
	}
	
}
