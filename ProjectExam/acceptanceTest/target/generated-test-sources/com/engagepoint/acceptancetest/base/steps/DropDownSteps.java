package com.engagepoint.acceptancetest.base.steps;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesSelectOneMenu;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

public class DropDownSteps extends ScenarioSteps{

	private static final long serialVersionUID = 1L;
	
	@Steps
	private JbehaveBaseSteps jbehaveBase;

	private PrimeFacesSelectOneMenu dropDown;
	
	public DropDownSteps(Pages pages) {
		super(pages);
	}
	
	@Given("user find drop-down root selector $root")
	public void givenUserDropDownRootSelector(String root) {
		By by = jbehaveBase.findVisibleElementAndGetSelector(root);
		WebDriver driver = getDriver();
		dropDown = new PrimeFacesSelectOneMenu(driver, driver.findElement(by),
				TimeUnit.MILLISECONDS.convert(pages().getConfiguration()
						.getElementTimeout(), TimeUnit.SECONDS));
	}
	
	@When("select '$optionText' option in drop-down")
	public void whenSelectOptionInDropDown(String optionText){
		dropDown.select(optionText);		
	}
	
	@Then("verify that '$optionText' is selected in drop-down")
	public void thenVerifyThatSelctedOptionIs(String optionText){
		assertThat(dropDown.getSelected(), is(optionText));
	}
	
	@When("select '$option' from '$selector' drop-down")
	public void whenSelectOptionFromDropDown(String option, String selector){
		givenUserDropDownRootSelector(selector);
		whenSelectOptionInDropDown(option);
		thenVerifyThatSelctedOptionIs(option);
	}
	
}
