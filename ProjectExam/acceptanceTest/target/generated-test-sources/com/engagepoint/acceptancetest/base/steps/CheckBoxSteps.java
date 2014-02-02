package com.engagepoint.acceptancetest.base.steps;

import java.util.concurrent.TimeUnit;

import org.jbehave.core.annotations.When;
import org.junit.Assume;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.engagepoint.acceptancetest.base.pagefragments.CheckBoxWidget;
import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesCheckBox;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

public class CheckBoxSteps extends ScenarioSteps{

	private static final long serialVersionUID = 1L;
	
	@Steps
	private JbehaveBaseSteps jbehaveBase;

	private transient CheckBoxWidget checkBox;
	
	public CheckBoxSteps(Pages pages) {
		super(pages);
	}
	
	@When("make '$selector' checked")
	public void whenMakeChecked(String selector) {
		getCheckBox(selector);
		Assume.assumeFalse(checkBox.isChecked());
		checkBox.click();
	}

	private void getCheckBox(String selector) {
		By by = jbehaveBase.findVisibleElementAndGetSelector(selector);
		WebDriver driver = getDriver();
		checkBox = new PrimeFacesCheckBox(driver, driver.findElement(by),
				TimeUnit.MILLISECONDS.convert(pages().getConfiguration()
						.getElementTimeout(), TimeUnit.SECONDS));
	}

	@When("make '$selector' unchecked")
	public void whenMakeUnchecked(String selector) {
		getCheckBox(selector);
		Assume.assumeTrue(checkBox.isChecked());
		checkBox.click();
	}

}