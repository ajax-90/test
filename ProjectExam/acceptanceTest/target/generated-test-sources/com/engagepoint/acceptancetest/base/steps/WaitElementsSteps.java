package com.engagepoint.acceptancetest.base.steps;

import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.xpathOrCssSelector;

import java.util.List;

import net.thucydides.core.pages.AnyPage;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.pages.WebElementFacade;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbehave.core.annotations.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.engagepoint.acceptancetest.base.webelements.utils.ExtendedExpectedConditions;

public class WaitElementsSteps extends ScenarioSteps {

	private static final long serialVersionUID = 1L;
	private static final long TO_MILISECONDS = 1000L;
	private AnyPage anyPage;

	public WaitElementsSteps(Pages pages) {
		super(pages);
		anyPage = pages().get(AnyPage.class);
		anyPage.setWaitForTimeout(pages().getConfiguration().getElementTimeout() * TO_MILISECONDS);
	}

	@Then("wait for element '$id' is not visible")
	public void waitForElementIsNotPresent(String id) {
		anyPage.waitForRenderedElementsToDisappear(By.id(id));
	}

	@Then("wait for element '$id' is visible")
	public void waitForElementIsPresent(String id) {
		anyPage.waitForRenderedElementsToBePresent(By.id(id));
	}

	@Then("wait for invisible element '$xpathOrCssSelector'")
	public void waitForElementIsNotVisibleByXpathOrCssSelector(String xpathOrCssSelector) {
		anyPage.waitForAbsenceOf(xpathOrCssSelector);
	}

	@Then("wait for visible element '$xpathOrCssSelector'")
	public void waitForElementIsVisibleByXpathOrCssSelector(String xpathOrCssSelector) {
		anyPage.waitFor(xpathOrCssSelector);
	}
	
	@Then("wait for title '$expectedTitle' contains")
	public void waitForTitleContains(String expectedTitle) {
		anyPage.waitFor(ExpectedConditions.titleContains(expectedTitle));
	}
	
	@Then("wait for invisible all elements '$xpathOrCssSelector'")
	public void thenWaitForInvisibleAllElementsByXpathOrCssSelector(String xpathOrCssSelector) {
		List<WebElementFacade> elementsFacades = anyPage.findAll(xpathOrCssSelector(xpathOrCssSelector));
		for(WebElementFacade el : elementsFacades) {
			el.waitUntilNotVisible();
		}
	}
	
	@Then("wait until page content animations completed")
	public void thenWaitUntilPageBodyAnimationsCompleted() {
		new WebDriverWait(getDriver(), getPages().getConfiguration()
				.getElementTimeout()).until(ExtendedExpectedConditions
				.elementHasNewCssStyle(".page-content", "left", "0px"));
	}

	@Then("wait until all animations on page completed")
	public void thenWaitUntilAllAnimationsOnPageCompleted() {
		new WebDriverWait(getDriver(), getPages().getConfiguration()
				.getElementTimeout()).until(ExtendedExpectedConditions
				.allAnimationsComplete());
	}

}
