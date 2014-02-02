package com.engagepoint.acceptancetest.base.webelements.utils;

import static ch.lambdaj.Lambda.convert;

import java.util.Iterator;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import ch.lambdaj.function.convert.Converter;

public final class WebElementsHelper {
	
	private WebElementsHelper() {}

	public static void removeUnvisibleElementsFrom(List<WebElement> elemenstList) {
		Iterator<WebElement> elementsIterator = elemenstList.iterator();
		while(elementsIterator.hasNext()) {
			WebElement heading = elementsIterator.next();
			if(!heading.isDisplayed()){
				elementsIterator.remove();
			}
		}
	}
	
	public static List<String> getTextValuesFrom(List<WebElement> elementsList) {
		return convert(elementsList, toTextValues());
	}
		
	private static Converter<WebElement, String> toTextValues() {
        return new Converter<WebElement, String>() {
            public String convert(WebElement from) {
                return from.getText();
            }
        };
    }
	
	public static WebElement getFirstVisibleElementFrom(List<WebElement> elemenstList) {
		removeUnvisibleElementsFrom(elemenstList);
		return elemenstList.get(0);
	}
	
	public static By xpathOrCssSelector(String xpathOrCssSelector) {
        if (isXPath(xpathOrCssSelector)) {
            return By.xpath(xpathOrCssSelector);
        } else {
            return By.cssSelector(xpathOrCssSelector);
        }
    }
	
	public static boolean isXPath(String xpathExpression) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        try {
            xpath.compile(xpathExpression);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
	
	public static String escapeJSId(String id) {
        return "#" + id.replaceAll(":", "\\\\\\\\:");
	}
	
	public static By findVisibleElementAndGetSelector(String id, SearchContext searchContex) {
		By[] selectors = { By.id(id), By.xpath("//*[contains(@id, '" + id + "')]"), By.name(id), By.className(id) };
		for (By selector : selectors) {
			if (isElementDisplayedInContext(selector, searchContex)) {
				return selector;
			}
		}
		return selectors[0];
	}
    
	public static boolean isElementDisplayedInContext(By selector, SearchContext searchContex) {
		try {
			return searchContex.findElement(selector).isDisplayed();
		} catch (Exception e) {
		}
		return false;
	}
	
}
