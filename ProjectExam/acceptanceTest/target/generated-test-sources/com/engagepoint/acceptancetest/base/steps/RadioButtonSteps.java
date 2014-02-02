package com.engagepoint.acceptancetest.base.steps;

import java.util.concurrent.TimeUnit;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.engagepoint.acceptancetest.base.pagefragments.RadioButtonGroupWidget;
import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesRadioButtonGroup;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

public class RadioButtonSteps extends ScenarioSteps{

	private static final long serialVersionUID = 1L;
	
	@Steps
	private JbehaveBaseSteps jbehaveBase;

	private transient RadioButtonGroupWidget radioButtonGroup;

	public RadioButtonSteps(Pages pages) {
		super(pages);
	}
	
	@Given("user find radiobutton group root selector $root")
	public void givenUserFindRadiobuttonGroupRootSelector(String root) {
		By by = jbehaveBase.findVisibleElementAndGetSelector(root);
		WebDriver driver = getDriver();
		radioButtonGroup = new PrimeFacesRadioButtonGroup(driver,
				driver.findElement(by), TimeUnit.MILLISECONDS.convert(pages()
						.getConfiguration().getElementTimeout(),
						TimeUnit.SECONDS));
	}
	
	@When("select '$option' radiobutton")
	public void whenSelectRadiobutton(String option) {
		radioButtonGroup.select(option);
	}

	@Then("verify that only '$option' is selected")
	public void thenVerifyThatAllRadioButtonsIsSelectedExcept(String option) {
		for(String label : radioButtonGroup.getAllRadioBtnLabels()){
			if(!option.equals(label)){
				Assert.assertFalse(radioButtonGroup.isSelected(label));
			}
		}
	}
}
