package com.engagepoint.acceptancetest.base.pagefragments;

import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.getTextValuesFrom;
import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.removeUnvisibleElementsFrom;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.engagepoint.acceptancetest.base.webelements.utils.ExtendedExpectedConditions;

public class PickList {

	private WebElement root;
	private WebDriver driver;
	private long timeOutInSeconds = 5;

	public PickList(WebElement root, WebDriver driver, long timeOutForAnimation) {
		this.root = root;
		this.driver = driver;
		this.timeOutInSeconds = timeOutForAnimation;
	}

	public void clickAdd() {
		clickByRelativeCss("button-add");
		waitUntilAllAnimationsComplete();
	}
	private void waitUntilAllAnimationsComplete() {
		new WebDriverWait(driver, timeOutInSeconds).until(ExtendedExpectedConditions.allAnimationsComplete());
	}

	private void clickByRelativeCss(String relativeCssPart) {
		findByRelativeCss(relativeCssPart).click();
	}

	private WebElement findByRelativeCss(String relativeCssPart) {
		return root.findElement(By.cssSelector(".ui-picklist-" + relativeCssPart));
	}

	public void clickRemove() {
		clickByRelativeCss("button-remove");
		waitUntilAllAnimationsComplete();
	}
	
	public void clickAddAll() {
		clickByRelativeCss("button-add-all");
		waitUntilAllAnimationsComplete();
	}
	
	public void clickRemoveAll() {
		clickByRelativeCss("button-remove-all");
		waitUntilAllAnimationsComplete();
	}
	
	public List<String> getSourceItems() {
		return getTextValuesFrom(getItemsFrom("source"));
	}

	private List<WebElement> getItemsFrom(String listName) {
		List<WebElement> items = findByRelativeCss(listName).findElements(By.cssSelector(".ui-picklist-item"));
		removeUnvisibleElementsFrom(items);
		return items;
	}

	public List<String> getTargetItems() {
		return getTextValuesFrom(getItemsFrom("target"));
	}
	
	public void selectByText(List<String> itemsText) {
		if (itemsText.size() > 1) {
			Actions multipleSelect = new Actions(driver).keyDown(Keys.CONTROL);
			for (String text : itemsText) {
				selectItemByJScript(text);//This is HACK //Bug: https://code.google.com/p/selenium/issues/detail?id=3734
			}
			multipleSelect.keyUp(Keys.CONTROL).build().perform();
		} else {
			getItemByText(itemsText.get(0)).click();
		}
		
	}

	private void selectItemByJScript(String text) {
		((JavascriptExecutor) driver).executeScript("$('[data-item-label=\"" + text + "\"]').addClass('ui-state-highlight')");
	}

	private WebElement getItemByText(String text) {
		return findByRelativeCss("list .ui-picklist-item[data-item-label=\"" + text + "\"]");
	}
	
	public void dragAndDropToOtherList(String text){
		WebElement element = getItemByText(text);
		String listClass = element.findElement(By.xpath("./..")).getAttribute("class");
		WebElement target;
		if(listClass.contains("ui-picklist-target")) {
			target = findByRelativeCss("source");
		} else {
			target = findByRelativeCss("target");
		}
		doDragAndDrop(element, target);
	}

	private void doDragAndDrop(WebElement element, WebElement target) {
		new Actions(driver).dragAndDrop(element, target).perform();
		waitUntilAllAnimationsComplete();
	}

	public void doubleClickOn(String item) {
		new Actions(driver).doubleClick(getItemByText(item)).perform();
		waitUntilAllAnimationsComplete();
	}

	public void dragAndDropToPosition(String text, int position) {
		WebElement element = getItemByText(text);
		WebElement target = element.findElement(By.xpath("./../*[" + position + "]"));
		int elementpositionAtList = element.findElements(By.xpath("./preceding-sibling::*")).size() + 1;
		Dimension size = target.getSize();
		new Actions(driver).clickAndHold(element).moveToElement(target, size.width / 2, elementpositionAtList > position ? 1 : size.height).release().perform();
		waitUntilAllAnimationsComplete();
	}

	public void clickMoveCommandInList(String command, String targetOrSource) {
		String cssRealtiveSelector = String.format("%s-controls .ui-picklist-button-move-%s", targetOrSource, command);
		clickByRelativeCss(cssRealtiveSelector);
		waitUntilAllAnimationsComplete();
	}

	public void searchInList(String searchText, String targetOrSource) {
		WebElement list = findByRelativeCss(targetOrSource);
		WebElement filterContainer = list.findElement(By.xpath("./../*[contains(@class, 'ui-picklist-filter-container')]"));
		WebElement searchFiled = filterContainer.findElement(By.cssSelector(".ui-inputfield"));
		searchFiled.sendKeys(searchText);
		waitUntilAllAnimationsComplete();
	}
	
	 

}
