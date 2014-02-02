package com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets;

import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.getTextValuesFrom;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.engagepoint.acceptancetest.base.pagefragments.RadioButtonGroupWidget;

import net.thucydides.core.pages.WebElementFacadeImpl;

public class PrimeFacesRadioButtonGroup extends WebElementFacadeImpl implements RadioButtonGroupWidget {

	private WebElement root;
	
	public PrimeFacesRadioButtonGroup(WebDriver driver,
			WebElement webElement, long timeoutInMilliseconds) {
		super(driver, webElement, timeoutInMilliseconds);
		this.root = webElement;
	}

	public void select(String option) {
		WebElement radiobuttonForSelect = getButtonFor(option);
		radiobuttonForSelect.click();
	}

	private WebElement getButtonFor(String option) {
		String forAttribute = root.findElement(By.xpath(".//label[text()='" + option + "']")).getAttribute("for");
		return root.findElement(By.xpath(".//*[@id='" + forAttribute + "']/ancestor::*[contains(@class, 'ui-radiobutton')]"));
	}

	public boolean isSelected(String option) {
		WebElement radiobuttonForSelect = getButtonFor(option);
		WebElement radioButtonIcon = radiobuttonForSelect.findElement(By.cssSelector(".ui-radiobutton-icon"));
		return radioButtonIcon.getAttribute("class").contains("ui-icon-bullet");
	}
	
	public List<String> getAllRadioBtnLabels() {
		return getTextValuesFrom(root.findElements(By.tagName("label")));
	}

}
