package com.engagepoint.acceptancetest.base.steps;

import java.util.concurrent.TimeUnit;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.engagepoint.acceptancetest.base.pagefragments.CalendarWidget;
import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesBasicCalendar;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

public class CalendarSteps extends ScenarioSteps{

	private static final long serialVersionUID = 1L;
	
	private transient CalendarWidget calendar;
	
	@Steps
	private JbehaveBaseSteps jbehaveBase;

	public CalendarSteps(Pages pages) {
		super(pages);
	}
	
	@Given("user find calendar root selector $root")
	public void givenUserFindCalendarRootSelector(String root) {
		By by = jbehaveBase.findVisibleElementAndGetSelector(root);
		WebDriver driver = getDriver();
		calendar = new PrimeFacesBasicCalendar(driver, driver.findElement(by),
				TimeUnit.MILLISECONDS.convert(pages().getConfiguration()
						.getElementTimeout(), TimeUnit.SECONDS));
	}
	
	@When("choose date '$date'")
	public void whenChooseDate(String date) {
		calendar.chooseDate(date);
	}
	
	@When("choose date $date from datepicker")
	public void whenChooseDateFromDatePicker(String date) {
		calendar.useDatePickerForChooseDate(date);
	}

	@Then("selected date $expectedDate appears in the calendar field")
	public void thenSelectedDatedateAppearsInTheCalendarField(String expectedDate) {
		calendar.verifyThatDate(expectedDate);
	}

}
