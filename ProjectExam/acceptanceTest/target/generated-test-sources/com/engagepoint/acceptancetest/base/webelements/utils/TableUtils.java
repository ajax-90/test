package com.engagepoint.acceptancetest.base.webelements.utils;

import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.getTextValuesFrom;
import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.removeUnvisibleElementsFrom;
import static net.thucydides.core.matchers.BeanMatchers.the;
import static net.thucydides.core.pages.components.HtmlTable.inTable;
import static org.hamcrest.Matchers.is;

import java.util.List;

import net.thucydides.core.pages.components.HtmlTable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public final class TableUtils {
	
	private TableUtils() {}

	public static HtmlTable removeUnvisibleHeadingsFrom(WebElement tableElement) {
		List<WebElement> allHeadings = inTable(tableElement).headingElements();
		removeUnvisibleElementsFrom(allHeadings);
		return new HtmlTable(tableElement, getTextValuesFrom(allHeadings));
	}

	public static WebElement getFirstRowWithParametrsFromTable(
			WebElement table, String columnName, String firstValue) {
		return inComplexTable(table).findFirstRowWhere(the(columnName, is(firstValue)));
	}
	
	public static WebElement getFirstHeadingWithParametrsFromTable(WebElement table, String columnName) {
		return table.findElement(By.xpath(".//th[@role='columnheader' and contains(.,'" + columnName + "')]"));
	}

	public static HtmlTable inComplexTable(WebElement table) {
		List<String> headingsByRole = getHeadingsByRole(table);
		if (!headingsByRole.isEmpty()) {
			return new HtmlTable(table, headingsByRole);
		} else {
			return new HtmlTable(table);
		}
	}

	private static List<String> getHeadingsByRole(WebElement table) {
		return getTextValuesFrom(table.findElements(By
				.xpath(".//th[@role='columnheader']")));
	}

}
