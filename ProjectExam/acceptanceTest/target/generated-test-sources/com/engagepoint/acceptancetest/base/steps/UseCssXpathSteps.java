package com.engagepoint.acceptancetest.base.steps;

import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.xpathOrCssSelector;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UseCssXpathSteps extends ScenarioSteps {

	private static final long serialVersionUID = 1L;
	private UIBootstrapBasePage uiBootstrapBasePage;

	public UseCssXpathSteps(Pages pages) {
		super(pages);
        uiBootstrapBasePage = pages().get(UIBootstrapBasePage.class);
	}
    
    @When("execute javascript '$script'")
    public void executeJavaScriptOnPage(String script) {
        uiBootstrapBasePage.evaluateJavascript(script);
    }
    
	@When("clicks on element by '$xpathOrCss'")
	public void clickByXpathOrCss(String xpathOrCss) {
        uiBootstrapBasePage.element(xpathOrCss).click();
	}
	
	@When("mouseover on element by '$xpathOrCssSelector'")
	public void whenMouseoverOnElementByxpathOrCss(String xpathOrCssSelector) {
		WebDriver driver = uiBootstrapBasePage.getDriver();
		WebElement toElement = driver.findElement(xpathOrCssSelector(xpathOrCssSelector));
		new Actions(driver).moveToElement(toElement).perform();
	}
	
	@Then("element with '$xpathOrCss' has text '$textContent'")
	public void thenElementWithXpathOrCssHasText(String xpathOrCss, String textContent) {
		assertThat(uiBootstrapBasePage.element(xpathOrCss).getText(), is(equalTo(textContent)));
	}
	
}
