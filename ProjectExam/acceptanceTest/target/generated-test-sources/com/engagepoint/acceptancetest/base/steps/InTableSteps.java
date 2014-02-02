package com.engagepoint.acceptancetest.base.steps;

import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.pages.components.HtmlTable;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.engagepoint.acceptancetest.base.webelements.utils.ExtendedExpectedConditions.ajaxComplete;
import static com.engagepoint.acceptancetest.base.webelements.utils.TableUtils.*;
import static net.thucydides.core.matchers.BeanMatchers.the;
import static org.hamcrest.Matchers.*;

public class InTableSteps extends ScenarioSteps {

	private static final long serialVersionUID = 1L;

	private static final String DESCENDING_SORTING_TYPE = "Descending";

	private static final String ASCENDING_SORTING_TYPE = "Ascending";

	private static final Logger LOGGER = LoggerFactory.getLogger(InTableSteps.class);
	
	private static final String TABLE_TAG = "table";

	@Steps
	private	JbehaveBaseSteps jbehaveBase;

	private UIBootstrapBasePage uIBootstrapBasePage;

	public InTableSteps(Pages pages) {
		super(pages);
        uIBootstrapBasePage = pages().get(UIBootstrapBasePage.class);
	}

	@When("in table '$tablePartId' press '$iconName' icon")
	@Alias("in table <tablePartId> press <iconName> icon")
	public void pressIconInTableByName(
			@Named("tablePartId") String tablePartId,
			@Named("iconName") String iconName) {
		WebElement tableElement = getTableElement(tablePartId);
        uIBootstrapBasePage.element(tableElement)
				.then(".//*[contains(@class,'ui-icon-"
						+ iconName + "')]").click();
	}

	private WebElement getTableElement(String id) {
		By tableBy = jbehaveBase.findVisibleElementAndGetSelector(id);
		WebElement tableElement = uIBootstrapBasePage.getDriver().findElement(tableBy);
		if(!tableElement.getTagName().contentEquals(TABLE_TAG))
			tableElement = tableElement.findElement(By.tagName(TABLE_TAG));
		return tableElement;
	}

	protected WebElement getRowWithParametersFromTable(String tablePartId,
			String firstColumnName, String firstValue) {
		WebElement tableElement = getTableElement(tablePartId);
		WebElement targetRow = getFirstRowWithParametrsFromTable(tableElement,
				firstColumnName, firstValue);
		return targetRow;
	}

	@When("in table '$tablePartId' press '$iconName' icon for row with '$firstColumnName' '$firstValue'")
	public void pressIconForSpecificRowInTable(String tablePartId, String firstColumnName, String firstValue, String iconName) {
		WebElement targetRow = getRowWithParametersFromTable(tablePartId, firstColumnName, firstValue);
		WebElement iconLink = targetRow.findElement(By.xpath(".//*[contains(@class, 'ui-icon-" + iconName + "')]"));
		iconLink.click();
	}
	
	@When("in table '$tablePartId' press '$buttonText' button for row with '$firstColumnName' '$firstValue'")
	public void pressButtonForSpecificRowInTable(String tablePartId, String firstColumnName, String firstValue, String buttonText) {
		WebElement targetRow = getRowWithParametersFromTable(tablePartId, firstColumnName, firstValue);
		WebElement button = targetRow.findElement(By.xpath(".//*[@class='ui-button-text' and text()='" + buttonText + "']/.."));
		button.click();
	}

	@When("in table '$tablePartId' press '$linkName' link for row with '$firstColumnName' '$firstValue'")
	public void pressLinkForSpecificRowInTable(String tablePartId,
			String firstColumnName, String firstValue, String linkName) {
		WebElement targetRow = getRowWithParametersFromTable(tablePartId,
				firstColumnName, firstValue);
		WebElement link = targetRow.findElement(By.xpath(".//a[contains(.,'"
				+ linkName + "')]"));
		link.click();
	}

	@Then("verify that in table '$tablePartId' present row with '$firstColumnName' '$firstValue' and '$secondColumnName' '$secondValue'")
	public void verifyThatRowWithSpecificParametrsIsPresentInTable(
			String tablePartId, String firstColumnName, String firstValue,
			String secondColumnName, String secondValue) {
		HtmlTable table = inComplexTable(getTableElement(tablePartId));
		table.shouldHaveRowElementsWhere(the(firstColumnName, is(firstValue)),
				the(secondColumnName, is(secondValue)));
	}
	
	@Then("verify that in table '$tablePartId' row with '$firstColumnName' '$firstValue' is absent")
	public void verifyThatRowWithSpecificParametrsIsAbsentInTable(
			String tablePartId, String firstColumnName, String firstValue) {
		HtmlTable table = inComplexTable(getTableElement(tablePartId));
		table.shouldNotHaveRowElementsWhere(the(firstColumnName, is(firstValue)));
	}
	
	@Then("verify that in table '$tablePartId' present row where '$firstColumnName' is one of '$values'")
	public void verifyThatInTableIsPresentRowWithOnOfValuesInColumn(String tablePartId, String firstColumnName, List <String> values) {
		HtmlTable table = inComplexTable(getTableElement(tablePartId));
		table.shouldHaveRowElementsWhere(the(firstColumnName, isIn(values)));
	}
	
	@When("in table '$tableId' sorts records by '$columnName' '$sortingType'")
	public void whenInTableSortsRecordsByColumnName(String tableId, String columnName, String sortingType) {
		String sortingTypeMarker = "ui-icon-carat-2-n-s";
		if (ASCENDING_SORTING_TYPE.equalsIgnoreCase(sortingType)) {
			sortingTypeMarker = "ui-icon-triangle-1-n";
		} else if (DESCENDING_SORTING_TYPE.equalsIgnoreCase(sortingType)){
			sortingTypeMarker = "ui-icon-triangle-1-s";
		}
		WebElement sortableIconElement = getSortableIconElementForColumnInTable(tableId, columnName);
		while (!sortableIconElement.getAttribute("class").contains(sortingTypeMarker)) {
			sortableIconElement.click();
            uIBootstrapBasePage.waitFor(ajaxComplete());
			sortableIconElement = getSortableIconElementForColumnInTable(tableId, columnName);
		}
	}

	private WebElement getSortableIconElementForColumnInTable(String tableId, String columnName) {
		return getFirstHeadingWithParametrsFromTable(getTableElement(tableId), columnName).findElement(By.cssSelector(".ui-sortable-column-icon"));
	}

	@Then("verify that in table '$tableId' elements in column '$columnName' is sorted by '$sortingType'")
	public void thenVerifyThatInTableElemetsInColumnIsSortedBy(String tableId, String columnName, String sortingType) {
		HtmlTable table = inComplexTable(getTableElement(tableId));
		List<String> cells = getColumnCellsFromTable(columnName, table);
		List<String> sortedCopy = new ArrayList<String>(cells);
		if (ASCENDING_SORTING_TYPE.equalsIgnoreCase(sortingType)) {
			Collections.sort(sortedCopy);
		} else if (DESCENDING_SORTING_TYPE.equalsIgnoreCase(sortingType)){
			Collections.sort(sortedCopy, Collections.reverseOrder());
		} else {
			sortedCopy.removeAll(cells);
			LOGGER.error("Not valid sorting type: " + sortingType);
			LOGGER.error("Valid types are only: " + ASCENDING_SORTING_TYPE + "or" + DESCENDING_SORTING_TYPE);
		}
		Assert.assertEquals(sortedCopy, cells);		
	}

	private List<String> getColumnCellsFromTable(String columnName,
			HtmlTable table) {
		List<Map<Object, String>> rows = table.getRows();
		List<String> cells = new ArrayList<String>();
		for (Map<Object, String> row : rows) {
			for (Entry<Object, String> entry : row.entrySet()) {
				if (entry.getKey().toString().contentEquals(columnName)) {
					cells.add(entry.getValue());
				}
			}
		}
		return cells;
	}
	
	@When("in table '$tableId' chooses to display '$number' elements per page")
	public void whenInTableChoosesToDisplayNumberElementsPerPage(String tableId, String number) {
		WebElement rppSelect = getTableElement(tableId).findElement(By.cssSelector(".ui-paginator-rpp-options"));
        uIBootstrapBasePage.selectFromDropdown(rppSelect, number);
	}

	@Then("verify that in table '$tableId' is displayed '$number' elements per page")
	public void thenVerifyThatInTableIsDisplayedNumberElementsPerPage(String tableId, int number) {
		int rowQty = getRowsQty(tableId);
		Assert.assertThat(rowQty, is(lessThanOrEqualTo(number)));
	}

	private int getRowsQty(String tableId) {
		return inComplexTable(getTableElement(tableId)).getRowElements().size();
	}
	
	@Then("verify that table '$tableId' is empty")
	public void thenVerifyThatTableIsEmpty(String tableId) {
		int rowQty = getRowsQty(tableId);
		Assert.assertThat(rowQty, is(equalTo(0)));
	}
}
