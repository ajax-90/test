package com.engagepoint.acceptancetest.base.steps;

import com.engagepoint.acceptancetest.base.fileupload.FileToUploadOnRemoteDriver;
import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import net.thucydides.core.Thucydides;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.Set;

import static com.engagepoint.acceptancetest.base.webelements.utils.ExtendedExpectedConditions.anyWindowOtherThan;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class JbehaveBaseSteps extends ScenarioSteps {
	
	private static final long serialVersionUID = 1L;

	private static final String XPATH_SELCTOR_SUFIX = "')]";

	private static final String WINDOW_OPEN_JSCRIPT_COMMAND = "window.open()";

	private static final String CURRENT_LINK_KEY = "currentLink";
	
	private UIBootstrapBasePage uIBootstrapBasePage;

	private String baseWindowHandle;

	public JbehaveBaseSteps(Pages pages) {
		super(pages);
		uIBootstrapBasePage = pages().get(UIBootstrapBasePage.class);
	}
	
	@When("the user opens the default page")
    public void openLink() {
        uIBootstrapBasePage.open();
    }
	
	@When("the user opens the '$link' link")
    public void openLink(String link) {
        uIBootstrapBasePage.openAt(link);
    }
	
	@When("the user opens the '$contextPath' page")
    public void openContextPath(String contextPath) {
		String baseUrl = pages().getConfiguration().getBaseUrl();
		int lastSlash = baseUrl.lastIndexOf('/');
		String url = baseUrl.substring(0, lastSlash + 1);
        uIBootstrapBasePage.openAt(url + contextPath);
    }
	
	@When("the user opens saved link")
	@Alias("the user opens saved URL")
    public void openCurrentLink() {
		String currentLink = (String) Thucydides.getCurrentSession().get(CURRENT_LINK_KEY);
        uIBootstrapBasePage.openAt(currentLink);
    }
	
	@SuppressWarnings("unchecked")
	@Given("saved '$link' link")
    public void setupLink(String link) {
		Thucydides.getCurrentSession().put(CURRENT_LINK_KEY, link);		
    }
	
    @When("clicks on element with id/name/className '$id'")
    @Alias("the user clicks on element with id/name/className '$id'")
    public void clickBySelector(String id) {
        uIBootstrapBasePage.element(findVisibleElementAndGetSelector(id)).click();
    }

	public By findVisibleElementAndGetSelector(String id) {
		By[] selectors = { By.id(id), By.xpath("//*[contains(@id, '" + id + XPATH_SELCTOR_SUFIX), By.name(id), By.className(id) };
		for (By selector : selectors) {
			if (isElementDisplayed(selector)) {
				return selector;
			}
		}
		return selectors[0];
	}
    
	private boolean isElementDisplayed(By selector) {
		try {
			return uIBootstrapBasePage.element(selector).isCurrentlyVisible();
		} catch (Exception e) {
		}
		return false;
	}
    
	@When("press '$text' button")
	public void clickOnButtonText(String text) {
        uIBootstrapBasePage.element("//button[contains(.,'" + text + XPATH_SELCTOR_SUFIX).click();
	}
    
    @Then("the user is brought to the page with '$title' title")
    @Alias("should open page with '$title' title")
    public void verifyThatTitleIsPresentOnPage(String title) {
    	assertThat(uIBootstrapBasePage.getTitle(), is(equalTo(title)));
    }
    
    @When("the user fills '$id' field with '$value'")
    @Alias("'$id' field with '$value'")
    public void fillField(String id, String value) {
        uIBootstrapBasePage.enter(value).intoField(findVisibleElementAndGetSelector(id));
    }

    @When("the user clicks link with text '$text'")
    @Aliases(values={"clicks link with text '$text'", "clicks link with text <text>"})
    public void clickLinkByText(@Named("text") String text) {
        uIBootstrapBasePage.element("//a[contains(.,'" + text + XPATH_SELCTOR_SUFIX).click();
    }

    @When("chooses text '$option' from '$id' drop-down")
    public void selectListBoxValue(String option, String id) {
        uIBootstrapBasePage.element(findVisibleElementAndGetSelector(id)).selectByVisibleText(option);
    }
    
    @Then("in '$id' drop-down is selected text '$option' option")
    public void verifyListBoxSelectedValue(String id, String option) {
        assertThat(uIBootstrapBasePage.element(findVisibleElementAndGetSelector(id)).getSelectedVisibleTextValue(), is(option));
    }
    
    @When("the checkbox id/name/className '$id' is unchecked make it checked")
    public void setCheckBoxOn(String id) {
        uIBootstrapBasePage.setCheckbox(getCheckBoxWithName(id), true);
    }

	private WebElement getCheckBoxWithName(String id) {
		return uIBootstrapBasePage.getDriver().findElement(findVisibleElementAndGetSelector(id));
	}

    @When("the checkbox id/name/className '$id' is checked make it unchecked")
    public void setCheckBoxOff(String id) {
        uIBootstrapBasePage.setCheckbox(getCheckBoxWithName(id), false);
    }

    @When("the user clicks radio button with label '$labelText'")
    public void clickRadioButtonByLabel(String labelText) {
    	String forAttribute = uIBootstrapBasePage.getDriver().findElement(By.xpath("//label[text()='" + labelText + "']")).getAttribute("for");
        uIBootstrapBasePage.element("//*[@for='" + forAttribute + "']").click();
    }

    @Then("the link with text '$text' should be on the page")
    public void isTextLinkAccessible(String text) {
        uIBootstrapBasePage.element("//a[contains(.,'" + text + XPATH_SELCTOR_SUFIX).shouldBeCurrentlyVisible();
    }
    
    @Deprecated
    @Then("the text '$text' should be in the page source")
    @Alias("the text <text> should be in the page source")
    public void isTextAccessible(@Named("text") String text) {
    	assertThat(uIBootstrapBasePage.containsText(text), is(true));
    }

    @Then("element '$id' has attribute value '$value'")
    public void verifyThatElementHasValue(String id, String value) {
    	assertThat(uIBootstrapBasePage.element(findVisibleElementAndGetSelector(id)).getValue(), is(equalTo(value)));
    }
    
    @Then("element id/name/className '$id' has text '$textContent'")
    public void verifyThatElementHasText(String id, String textContent) {
    	assertThat(uIBootstrapBasePage.element(findVisibleElementAndGetSelector(id)).getText().replaceAll("\n", " "), is(equalTo(textContent)));
    }

    @Then("element id/name/className '$id' contains text '$textContent'")
    public void verifyThatElementContainsText(String id, String textContent) {
        assertThat(uIBootstrapBasePage.element(findVisibleElementAndGetSelector(id)).getText().replaceAll("\n", " "), is(containsString(textContent)));
    }
    
    @When("the user uploads the fileName/filePath '$file' to field with '$id'")
    public void uploadFile(String file, String id) {
    	WebElement fileField = uIBootstrapBasePage.getDriver().findElement(findVisibleElementAndGetSelector(id));
    	if(ThucydidesSystemProperty.REMOTE_URL.isDefinedIn(pages().getConfiguration().getEnvironmentVariables())){
            new FileToUploadOnRemoteDriver(file).to(fileField);
        } else {
            uIBootstrapBasePage.upload(file).to(fileField);
        }
    }
    
    @When("clicks on id/name/className '$id' and confirm browser alert")
    public void clickBySelectorAndConfirmAlert(String id) {
		clickBySelector(id);
        uIBootstrapBasePage.getAlert().accept();
    }
	
	@When("clicks on id/name/className '$id' and dismiss browser alert")
    public void clickBySelectorAndDismissAlert(String id) {
		clickBySelector(id);
        uIBootstrapBasePage.getAlert().dismiss();
    }

	@When("user presses backspace at '$id' field")
    public void pressBackspaceAtField(String id) {
        uIBootstrapBasePage.getDriver().findElement(findVisibleElementAndGetSelector(id)).sendKeys(Keys.BACK_SPACE);
    }
	
	@SuppressWarnings("unchecked")
	@When("the user save current URL")
	public void saveCurrentUrl() {
		Thucydides.getCurrentSession().put(CURRENT_LINK_KEY, uIBootstrapBasePage.getDriver().getCurrentUrl());
	}
	
	@When("open saved URL in new TAB and switch to it")
	public void openUrlInNewTabAndSwitchToIt() {
		String currentUrl = (String) Thucydides.getCurrentSession().get(CURRENT_LINK_KEY);
        openUrlInNewTab(currentUrl);
	}

    protected void openUrlInNewTab(String currentUrl) {
        uIBootstrapBasePage.evaluateJavascript(WINDOW_OPEN_JSCRIPT_COMMAND);
        switchToNewWindow();
        uIBootstrapBasePage.openAt(currentUrl);
    }

    @When("return to base window")
	public void returnToBaseWindow () {
		WebDriver driver = uIBootstrapBasePage.getDriver();
		if(!baseWindowHandle.isEmpty() && !driver.getWindowHandle().equals(baseWindowHandle)) {
			driver.close();
			driver.switchTo().window(baseWindowHandle);
		}
	}
	
	@When("the user open link with text '$text' in new window")
	public void whenTheUserOpenLinkWithTextInNewWindow(String text) {
		String hrefAttribute = uIBootstrapBasePage.getDriver().findElement(By.linkText(text)).getAttribute("href");
		String openLinkInNewWindowJScript = String.format("window.open('%s', '_blank')", hrefAttribute);
        uIBootstrapBasePage.evaluateJavascript(openLinkInNewWindowJScript);
	}

	@When("switch to new window")
	public void switchToNewWindow() {
		WebDriver driver = uIBootstrapBasePage.getDriver();
		baseWindowHandle = driver.getWindowHandle();
		Set <String> openedWindows = driver.getWindowHandles();
		String newWindow = null;
		if (openedWindows.size() > 1 && openedWindows.remove(baseWindowHandle)) {
			Iterator<String> openedWindowsIterator = openedWindows.iterator();	
			newWindow = (String) openedWindowsIterator.next();
		} else {
			int timeout = pages().getConfiguration().getElementTimeout();
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			newWindow = wait.until(anyWindowOtherThan(openedWindows));
		}
		driver.switchTo().window(newWindow);
	}
}
