package com.engagepoint.acceptancetest.base.steps;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;


import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.engagepoint.acceptancetest.base.pagefragments.PickList;

import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

public class PickListSteps extends ScenarioSteps{

	private static final long serialVersionUID = 1L;

	private PickList picklist;
	
	@Steps
	private JbehaveBaseSteps jbehaveBase;
	
	public PickListSteps(Pages pages) {
		super(pages);
	}
	
	@Given("user find picklist root selector $root")
	public void givenUserFindPicklistRootSelector(String root) {
		By by = jbehaveBase.findVisibleElementAndGetSelector(root);
		WebDriver driver = getDriver();
		picklist = new PickList(driver.findElement(by), driver, pages().getConfiguration().getElementTimeout());
	}

	@When("user select $item")
	public void whenUserSelect(List<String> items) {
		picklist.selectByText(items);
	}

	@When("press add")
	public void whenPressAdd() {
		picklist.clickAdd();
	}
	
	@When("press add all")
	public void whenPressAddAll() {
		picklist.clickAddAll();
	}
	
	@When("press remove")
	public void whenPressRemove() {
		picklist.clickRemove();
	}
	
	@When("press remove all")
	public void whenPressRemoveAll() {
		picklist.clickRemoveAll();
	}

	@Then("$item appeared in target list")
	public void thenInTargetListAppeared(List<String> items) {
		assertThat(picklist.getTargetItems(), equalTo(items));
	}
	
	@Then("$item appeared in source list")
	public void thenInSourceListAppeared(List<String> items) {
		assertThat(picklist.getSourceItems(), equalTo(items));
	}

	@When("user drag and drop $item to other list")
	public void whenUserDragAndDropToOtherList(String item) {
		picklist.dragAndDropToOtherList(item);
	}
	
	@When("user drag and drop $item to $position position")
	public void whenUserDragAndDropToPosition(String item, Integer position) {
		picklist.dragAndDropToPosition(item, position);
	}
	
	@When("user double click on $item")
	public void whenUserDoubleClickOn(String item) {
		picklist.doubleClickOn(item);
	}
	
	@When("press move $command in $targetOrSource list")
	public void whenPressCommandInList(String command, String targetOrSource) {
		picklist.clickMoveCommandInList(command, targetOrSource);
	}
	
	@When("search $text in $targetOrSource list")
	public void whenSearchInList(String searchText, String targetOrSource) {
		picklist.searchInList(searchText, targetOrSource);
	}

}
