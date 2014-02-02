package com.engagepoint.acceptancetest.base.steps;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.engagepoint.acceptancetest.base.pagefragments.ModalWidget;
import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesConfirmDialog;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

public class ModalSteps extends ScenarioSteps {

	private static final long serialVersionUID = 1L;
	
	private transient ModalWidget modalDialog;
	
	@Steps
	private JbehaveBaseSteps jbehaveBase;

	public ModalSteps(Pages pages) {
		super(pages);
	}
	
	@Given("user find modal root selector $root")
	public void givenUserFindModalRootSelectorselector(String root) {
		By by = jbehaveBase.findVisibleElementAndGetSelector(root);
		WebDriver driver = getDriver();
		modalDialog = new PrimeFacesConfirmDialog(driver, driver.findElement(by),
				TimeUnit.MILLISECONDS.convert(pages().getConfiguration()
						.getElementTimeout(), TimeUnit.SECONDS));
	}
	
	@When("close modal dialog")
	public void whenCloseModalDialog() {
		modalDialog.close();
	}
	
	@When("press '$buttonText' in modal dialog")
	public void whenInModalDialogPress(String buttonText) {
		modalDialog.clickOn(buttonText);
	}
	
	@Then("verify that modal dialog is not visible")
	public void thenVerifyThatModalDialogIsNotVisible() {
		Assert.assertFalse(modalDialog.isVisible());
	}
	
	@Then("verify that modal dialog is visible")
	public void thenVerifyThatModalDialogIsVisible() {
		Assert.assertTrue(modalDialog.isVisible());
	}

}
