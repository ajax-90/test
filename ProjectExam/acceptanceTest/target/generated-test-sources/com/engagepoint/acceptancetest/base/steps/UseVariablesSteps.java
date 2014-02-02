package com.engagepoint.acceptancetest.base.steps;

import static com.engagepoint.acceptancetest.base.utils.TestSessionUtils.getVariableFromTestSessionIfExist;
import static com.engagepoint.acceptancetest.base.utils.TestSessionUtils.replaceMatcherByVariable;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.thucydides.core.Thucydides;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

public class UseVariablesSteps extends ScenarioSteps{
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UseVariablesSteps.class);

	public UseVariablesSteps(Pages pages) {
		super(pages);
	}
	
	@Steps
	private InTableSteps inTable;
	
	@Steps
	private JbehaveBaseSteps jbehaveBase;
	
	@SuppressWarnings("unchecked")
	@When("save text from '$id' to '$variableName' variable")
	public void whenSaveTextToVariable(String id, String variableName) {
		By byElement = jbehaveBase.findVisibleElementAndGetSelector(id);
		String textElement = jbehaveBase.getDriver().findElement(byElement).getText();
		Thucydides.getCurrentSession().put(variableName, textElement);
		LOGGER.info("Session variable " + variableName + " = " + textElement + " was saved.");
	}

	@When("the user fills '$id' field with '$variableName' variable")
	public void whenFillsFieldWithVariable(String id, String variableName) {
		String value = replaceMatcherByVariable(variableName);
		jbehaveBase.fillField(id, value);
	}
	
	@Then("element '$id' has text equal to '$variableName' variable")
	public void thenElementHasTextEqualToVariable(String id, String variableName) {
		String variable = replaceMatcherByVariable(variableName);
		By byElement = jbehaveBase.findVisibleElementAndGetSelector(id);
		String tagName = jbehaveBase.getDriver().findElement(byElement).getTagName();		
		if (tagName.contentEquals("textarea")) {
			jbehaveBase.verifyThatElementHasValue(id, variable);
		} else {
			jbehaveBase.verifyThatElementHasText(id, variable);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Given("unique string with prefix '$prefix' and '$length' length saved to '$variableName' variable")
	public void givenUniqueStringWithPrefixAndLengthSavedToVariable(String prefix, int length, String variableName) {
		String uniqueString = String.valueOf(UUID.randomUUID()).replace("-", "");
		String textElement = StringUtils.rightPad(prefix, length, uniqueString);
		Thucydides.getCurrentSession().put(variableName, textElement);
		LOGGER.info("Session variable " + variableName + " = " + textElement + " was saved.");
	}
	
	@Then("verify that in table '$tableId' present row with '$variableName' variable in '$columnName'")
	public void thenVerifyThatInTablePresentRowWithVariableInColumn(String tableId, String variableName, String columnName) {
		String value = getVariableFromTestSessionIfExist(variableName);
		inTable.verifyThatInTableIsPresentRowWithOnOfValuesInColumn(tableId, columnName, Arrays.asList(value));
	}
	
	@When("in table '$tablePartId' press '$linkName' link for row with '$firstColumnName' '$variableName' variable")
	public void whenInTablePressLinkForRowWithVariable(String tablePartId, String linkName, String firstColumnName, String variableName) {
		String firstValue = getVariableFromTestSessionIfExist(variableName);
		inTable.pressLinkForSpecificRowInTable(tablePartId, firstColumnName, firstValue, linkName);
	}
	
	@When("in table '$tablePartId' press '$iconName' icon for row with '$firstColumnName' '$variableName' variable")
	public void whenInTablePressIconForRowWithVariable(String tablePartId, String firstColumnName, String variableName, String iconName) {
		String firstValue = getVariableFromTestSessionIfExist(variableName);
		inTable.pressIconForSpecificRowInTable(tablePartId, firstColumnName, firstValue, iconName);
	}
}
