package com.engagepoint.acceptancetest.base.steps;

import java.util.concurrent.TimeUnit;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assume;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.engagepoint.acceptancetest.base.pagefragments.PanelWidget;
import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesPanel;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

public class PanelSteps extends ScenarioSteps{


	private static final long serialVersionUID = 1L;
	
	@Steps
	private JbehaveBaseSteps jbehaveBase;

	private transient PanelWidget panel;
	
	public PanelSteps(Pages pages) {
		super(pages);
	}
	
	@Given("user find panel root selector $root")
	public void givenUserFindPanelRootSelector(String root) {
		By by = jbehaveBase.findVisibleElementAndGetSelector(root);
		WebDriver driver = getDriver();
		panel = new PrimeFacesPanel(driver, driver.findElement(by),
				TimeUnit.MILLISECONDS.convert(pages().getConfiguration()
						.getElementTimeout(), TimeUnit.SECONDS));
	}
	
	@When("select '$option' option in panel")
	public void whenInPanelSelectOption(String option) {
		panel.chooseOption(option);
	}

	@Then("verify panel content is not visible")
	public void thenVerifyPanelContentIsNotVisible() {
		Assume.assumeFalse(panel.isContentVisible());
	}
	
	@When("toggle panel")
	public void whenTogglePanel() {
		panel.toggle();
	}

	@Then("verify panel content is visible")
	public void thenVerifyPanelContentIsVisible() {
		Assume.assumeTrue(panel.isContentVisible());
	}
	
	@When("close panel")
	public void whenClosePanel() {
		panel.close();
	}
}
