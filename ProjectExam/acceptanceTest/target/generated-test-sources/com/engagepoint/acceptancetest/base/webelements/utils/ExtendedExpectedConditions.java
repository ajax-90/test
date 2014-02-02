package com.engagepoint.acceptancetest.base.webelements.utils;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;

public final class ExtendedExpectedConditions {
	
	private ExtendedExpectedConditions() {
	}

	public static ExpectedCondition<String> anyWindowOtherThan(
			final Set<String> oldWindows) {
		return new ExpectedCondition<String>() {
			public String apply(WebDriver driver) {
				if (driver == null)
					throw new WebDriverException();
				Set<String> allWindows = driver.getWindowHandles();
				allWindows.removeAll(oldWindows);
				return allWindows.size() > 0 ? allWindows.iterator().next()
						: null;
			}
		};
	}

	public static ExpectedCondition<Boolean> ajaxComplete() {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				if (driver == null)
					throw new WebDriverException();
				long counter = (Long) ((JavascriptExecutor) driver)
						.executeScript("return window.jQuery.active",
								new Object[] {});
				return counter == 0 ? Boolean.TRUE : Boolean.FALSE;
			}
		};
	}
	
	public static ExpectedCondition<Boolean> allAnimationsComplete() {
		return animationCompletes("body *");
	}

	public static ExpectedCondition<Boolean> animationCompletes(final String selector) {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				if (driver == null)
					throw new WebDriverException();
				return (Boolean) ((JavascriptExecutor) driver)
						.executeScript("return ! $('" + selector + "').is(':animated');");
			}
		};
	}

    public static ExpectedCondition<Boolean> elementHasNewCssStyle(final String cssSelector,final String cssProperty, final String expectedPropertyValue) {
        return new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                if (driver == null)
                    throw new WebDriverException();
                String actualPropertyValue = driver.findElement(By.cssSelector(cssSelector)).getCssValue(cssProperty);
                return actualPropertyValue.equals(expectedPropertyValue);
            }
        };
    }

}
