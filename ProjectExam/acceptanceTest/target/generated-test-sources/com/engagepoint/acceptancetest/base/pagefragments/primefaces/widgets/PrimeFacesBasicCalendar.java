package com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.engagepoint.acceptancetest.base.pagefragments.CalendarWidget;

import net.thucydides.core.pages.WebElementFacadeImpl;

public class PrimeFacesBasicCalendar extends WebElementFacadeImpl implements CalendarWidget{
	
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final String EXTENDED_DATE_FORMAT = "MMMM/dd/yyyy";
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	private SimpleDateFormat extDateFormat = new SimpleDateFormat(EXTENDED_DATE_FORMAT);
	private WebElement root;
	private WebDriver driver;

	private WebElement datePicker;

	public PrimeFacesBasicCalendar(WebDriver driver, WebElement webElement,	long timeoutInMilliseconds) {
		super(driver, webElement, timeoutInMilliseconds);
		this.root = webElement;
		this.driver = driver;
	}

	public void chooseDate(String date) {
		WebElement dateField = getDateField();
		dateField.clear();
		dateField.sendKeys(date);
	}

	private WebElement getDateField() {
		return root.findElement(By.tagName("input"));
	}

	public void verifyThatDate(String expectedDate) {
		String actualDate = getDateField().getAttribute("value");
		Assert.assertEquals(expectedDate, actualDate);		
	}
	
	public void useDatePickerForChooseDate(String date) {
		Date selectedDate = parseDateString(date, dateFormat);
		WebElement showCalendarBtn = root.findElement(By.cssSelector(".ui-datepicker-trigger"));
		showCalendarBtn.click();
		waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(":not(.ui-datepicker-inline).ui-datepicker.ui-widget")));
		Calendar expCal = Calendar.getInstance();
		Calendar actualCal = Calendar.getInstance();
		expCal.setTime(selectedDate);
		actualCal.setTime(getActualTime());
		do {
		    if(actualCal.after(expCal)){
		    	clickPrevInDatePicker();		    	
		    } else if (actualCal.before(expCal)){
		    	clickNextInDatePicker();
		    }
		    actualCal.setTime(getActualTime());
		} while(actualCal.get(Calendar.YEAR) == expCal.get(Calendar.YEAR) ? actualCal.get(Calendar.MONTH) != expCal.get(Calendar.MONTH) : true);
		WebElement dayToSelect = datePicker.findElement(By.xpath(".//*[@data-handler='selectDay']/a[text()='" + expCal.get(Calendar.DAY_OF_MONTH) + "']"));
		dayToSelect.click();
	}

	private Date getActualTime() {
		datePicker = getDatePicker();
		String actualMonth = datePicker.findElement(By.cssSelector(".ui-datepicker-month")).getText();
		String actualYear = datePicker.findElement(By.cssSelector(".ui-datepicker-year")).getText();
		return parseDateString(actualMonth + "/01/" + actualYear, extDateFormat);
	}

	private WebElement getDatePicker() {
		return driver.findElement(By.cssSelector(":not(.ui-datepicker-inline).ui-datepicker.ui-widget"));
	}

	private void clickPrevInDatePicker() {
		WebElement prevBtn = datePicker.findElement(By.cssSelector(".ui-datepicker-prev"));
		prevBtn.click();
	}
	
	private void clickNextInDatePicker() {
		WebElement prevBtn = datePicker.findElement(By.cssSelector(".ui-datepicker-next"));
		prevBtn.click();
	}


	private Date parseDateString(String dateAsString, DateFormat dateFormat) {
		try {
			return dateFormat.parse(dateAsString);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Date parsing failed "
					+ dateAsString);
		}
	}

}
