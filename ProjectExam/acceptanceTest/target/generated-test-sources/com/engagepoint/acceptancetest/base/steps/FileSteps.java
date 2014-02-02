package com.engagepoint.acceptancetest.base.steps;

import com.engagepoint.acceptancetest.base.filedownload.FileDownloader;
import com.engagepoint.acceptancetest.base.filedownload.utils.RequestMethod;
import com.engagepoint.acceptancetest.base.filedownload.utils.RequestParameters;
import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import com.engagepoint.acceptancetest.base.pdf.PDFWords;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.apache.http.NameValuePair;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FileSteps extends ScenarioSteps{

	private static final long serialVersionUID = 1L;
    private UIBootstrapBasePage uiBootstrapBasePage;
    private File lastDownloadedFile;

    @Steps
    private JbehaveBaseSteps jbehaveBase;

    public FileSteps(Pages pages) {
		super(pages);
        uiBootstrapBasePage = pages().get(UIBootstrapBasePage.class);
	}

	@Then("verify that pdf file contains text '$textContent'")
	public void thenVerifyThatPdfFileContainsText(String textContent) throws IOException {
		PDFWords pdfLibrary = new PDFWords();
		pdfLibrary.parsePdf(lastDownloadedFile.getPath());
		pdfLibrary.pdfShouldContain(textContent);
	}

	@Then("verify that pdf file has '$count' occurrences of text '$textContent'")
	public void thenVerifyThatPdfFileHasOccurrencesOfText(int count, String textContent) throws IOException {
		PDFWords pdfLibrary = new PDFWords();
		pdfLibrary.parsePdf(lastDownloadedFile.getPath());
		pdfLibrary.pdfShouldContainOccurrencesOfString(textContent, count);
	}

    @When("the user clicks on '$locator' download file")
    public void whenTheUserClicksOnDownloadFile(String locator) throws IOException, URISyntaxException {
        FileDownloader downloadHandler = getFileDownloaderFor(locator);
        lastDownloadedFile = downloadHandler.downloadFile();
    }

    private FileDownloader getFileDownloaderFor(String locator) throws MalformedURLException, URISyntaxException {
        WebDriver driver = getDriver();
        FileDownloader downloadHandler = new FileDownloader(driver);
        By by = jbehaveBase.findVisibleElementAndGetSelector(locator);
        WebElement downloadBtn = uiBootstrapBasePage.element(by);
        String href = downloadBtn.getAttribute("href");
        String currentUrl = driver.getCurrentUrl();
        if(href != null && !href.equals(currentUrl + "#")){
            downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
            downloadHandler.setURI(href);
        } else {
            WebElement form = downloadBtn.findElement(By.xpath("./ancestor::form[@method='post']"));
            List<NameValuePair> requestParams = RequestParameters.getParametersFrom(form);
            String downloadBtnTag = downloadBtn.getTagName();
            if("button".equals(downloadBtnTag)){
                requestParams.addAll(RequestParameters.getParametersFrom(downloadBtn, "name"));
            } else {
                requestParams.addAll(RequestParameters.getParametersFrom(downloadBtn, "onclick"));
            }
            downloadHandler.setURI(currentUrl);
            downloadHandler.setHTTPRequestMethod(RequestMethod.POST);
            downloadHandler.setHttpPostRequestParams(requestParams);
        }
        return downloadHandler;
    }

    @Then("verify that file is downloaded")
    public void thenVerifyLastDownloadedFile(){
        assertThat(lastDownloadedFile.exists(), is(equalTo(true)));
    }

    @Then("verify that file could be downloaded by click on '$locator'")
    public void thenVerifyThatFileCouldBeDownloadedBy(String locator) throws IOException, URISyntaxException {
        FileDownloader downloadHandler = getFileDownloaderFor(locator);
        assertThat(downloadHandler.getLinkHTTPStatus(), is(equalTo(200)));
    }
}
