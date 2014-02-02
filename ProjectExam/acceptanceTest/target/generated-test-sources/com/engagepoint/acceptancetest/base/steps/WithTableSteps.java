package com.engagepoint.acceptancetest.base.steps;

import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.findVisibleElementAndGetSelector;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.engagepoint.acceptancetest.base.pagefragments.DataTableWithSubtable;
import com.engagepoint.acceptancetest.base.pagefragments.primefaces.widgets.PrimeFacesDataTableWithSubtable;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

public class WithTableSteps extends ScenarioSteps{
	
	private static final long serialVersionUID = 1L;

	private static final String TABLE_TAG = "table";
	
	@Steps
	private JbehaveBaseSteps jbehaveBase;

	private transient DataTableWithSubtable dataTableWithSubtable;

    public WithTableSteps(Pages pages) {
        super(pages);
    }
    
    @Given("user find data table with subtable root selector $root")
    public void givenUserFindDataTableWithSubtableRootSelector(String root) {
    	By tableBy = jbehaveBase.findVisibleElementAndGetSelector(root);
    	WebDriver driver = getDriver();
		WebElement tableElement = driver.findElement(tableBy);
		if(!tableElement.getTagName().contentEquals(TABLE_TAG))
			tableElement = tableElement.findElement(By.tagName(TABLE_TAG));
		dataTableWithSubtable = new PrimeFacesDataTableWithSubtable(driver, tableElement,
				TimeUnit.MILLISECONDS.convert(pages().getConfiguration()
						.getElementTimeout(), TimeUnit.SECONDS));
    }
    
	@When("in table click $locator $element for row with parameters: $paramsForFind")
	public void whenInTableClickElementForRowWithParameters(String locator,
			String element, ExamplesTable paramsForFind) {
		Map<String, String> rowToFind = paramsForFind.getRow(0);
		WebElement matchedRow = dataTableWithSubtable.findRowWithParameters(rowToFind);
		By clickElementBy;
		if ("button".equals(element)) {
			clickElementBy = By.xpath(".//" + element + "[text()='" + locator
					+ "' or @title='" + locator + "']");
		} else {
			clickElementBy = findVisibleElementAndGetSelector(locator,
					(SearchContext) matchedRow);
		}
		WebElement elementToClick = matchedRow.findElement(clickElementBy);
		elementToClick.click();
	}

}
