package com.engagepoint.acceptancetest.base.filedownload;

import com.engagepoint.acceptancetest.base.filedownload.utils.RequestMethod;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;

public class FileDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloader.class);
    public static final String TAG_IMG = "img";
    public static final String TAG_A = "a";
    private WebDriver driver;
    private boolean followRedirects = true;
    private boolean mimicWebDriverCookieState = true;
    private RequestMethod httpRequestMethod = RequestMethod.GET;
    private URI fileURI;
    private List<NameValuePair> httpPostRequestParams;

    public FileDownloader(WebDriver driverObject) {
        this.driver = driverObject;
    }

    /**
     * Specify if the FileDownloader class should follow redirects when trying to download a file
     * Default: true
     *
     * @param followRedirects boolean
     */
    public void followRedirectsWhenDownloading(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    /**
     * Mimic the cookie state of WebDriver (Defaults to true)
     * This will enable you to access files that are only available when logged in.
     * If set to false the connection will be made as an anonymouse user
     *
     * @param mimicWebDriverCookies boolean
     */
    public void mimicWebDriverCookieState(boolean mimicWebDriverCookies) {
        mimicWebDriverCookieState = mimicWebDriverCookies;
    }

    /**
     * Set the HTTP Request Method
     * Default: GET
     *
     * @param requestType RequestMethod
     */
    public void setHTTPRequestMethod(RequestMethod requestType) {
        httpRequestMethod = requestType;
    }

    /**
     * Specify a URL that you want to perform an HTTP Status Check upon/Download a file from
     *
     * @param linkToFile String
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public void setURI(String linkToFile) throws MalformedURLException, URISyntaxException {
        fileURI = new URI(linkToFile);
    }

    /**
     * Specify a URL that you want to perform an HTTP Status Check upon/Download a file from
     *
     * @param linkToFile URI
     * @throws MalformedURLException
     */
    public void setURI(URI linkToFile) throws MalformedURLException {
        fileURI = linkToFile;
    }

    /**
     * Specify a URL that you want to perform an HTTP Status Check upon/Download a file from
     *
     * @param linkToFile URL
     */
    public void setURI(URL linkToFile) throws URISyntaxException {
        fileURI = linkToFile.toURI();
    }

    public List<NameValuePair> getHttpPostRequestParams() {
        return httpPostRequestParams;
    }

    public void setHttpPostRequestParams(List<NameValuePair> httpPostRequestParams) {
        this.httpPostRequestParams = httpPostRequestParams;
    }

    /**
     * Perform an HTTP Status Check upon/Download the file specified in the href attribute of a WebElement
     *
     * @param anchorElement Selenium WebElement
     * @throws IllegalArgumentException, URISyntaxException
     */
    public void setURISpecifiedInAnchorElement(WebElement anchorElement) throws URISyntaxException {
        if (TAG_A.equals(anchorElement.getTagName())) {
            fileURI = new URI(anchorElement.getAttribute("href"));
        } else {
            throw new IllegalArgumentException("You have not specified an <a> element! Specified element is:" + anchorElement);
        }
    }

    /**
     * Perform an HTTP Status Check upon/Download the image specified in the src attribute of a WebElement
     *
     * @param imageElement Selenium WebElement
     * @throws IllegalArgumentException, URISyntaxException
     */
    public void setURISpecifiedInImageElement(WebElement imageElement) throws URISyntaxException {
        if (TAG_IMG.equals(imageElement.getTagName())) {
            fileURI = new URI(imageElement.getAttribute("src"));
        } else {
            throw new IllegalArgumentException("You have not specified an <img> element! Specified element is:" + imageElement);
        }
    }

    /**
     * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
     *
     * @param seleniumCookieSet Set&lt;Cookie&gt;
     */
    private BasicCookieStore mimicCookieState(Set<Cookie> seleniumCookieSet) {
        BasicCookieStore copyOfWebDriverCookieStore = new BasicCookieStore();
        for (Cookie seleniumCookie : seleniumCookieSet) {
            BasicClientCookie duplicateCookie = new BasicClientCookie(seleniumCookie.getName(), seleniumCookie.getValue());
            duplicateCookie.setDomain(seleniumCookie.getDomain());
            duplicateCookie.setSecure(seleniumCookie.isSecure());
            duplicateCookie.setExpiryDate(seleniumCookie.getExpiry());
            duplicateCookie.setPath(seleniumCookie.getPath());
            copyOfWebDriverCookieStore.addCookie(duplicateCookie);
        }

        return copyOfWebDriverCookieStore;
    }

    private HttpResponse getHTTPResponse() throws IOException {
        if (fileURI == null) throw new IllegalArgumentException("No file URI specified");

        HttpClient client = new DefaultHttpClient();
        BasicHttpContext localContext = new BasicHttpContext();

        //Clear down the local cookie store every time to make sure we don't have any left over cookies influencing the test
        localContext.setAttribute(ClientContext.COOKIE_STORE, null);

        LOGGER.info("Mimic WebDriver cookie state: " + mimicWebDriverCookieState);
        if (mimicWebDriverCookieState) {
            localContext.setAttribute(ClientContext.COOKIE_STORE, mimicCookieState(driver.manage().getCookies()));
        }

        HttpRequestBase requestMethod = httpRequestMethod.getRequestMethod();
        requestMethod.setURI(fileURI);
        HttpParams httpRequestParameters = requestMethod.getParams();
        httpRequestParameters.setParameter(ClientPNames.HANDLE_REDIRECTS, followRedirects);
        requestMethod.setParams(httpRequestParameters);
        if(httpRequestMethod == RequestMethod.POST){
            HttpPost httpPost = (HttpPost) requestMethod;
            httpPost.setEntity(new UrlEncodedFormEntity(getHttpPostRequestParams()));
        }
        LOGGER.info("Sending " + httpRequestMethod.toString() + " request for: " + fileURI);
        return client.execute(requestMethod, localContext);
    }

    /**
     * Gets the HTTP status code returned when trying to access the specified URI
     *
     * @return File
     * @throws IOException
     */
    public int getLinkHTTPStatus() throws IOException {
        HttpResponse fileToDownload = getHTTPResponse();
        int httpStatusCode;
        httpStatusCode = fileToDownload.getStatusLine().getStatusCode();
        fileToDownload.getEntity().getContent().close();
        return httpStatusCode;
    }

    /**
     * Download a file from the specified URI
     *
     * @return File
     * @throws IOException
     */
    public File downloadFile() throws IOException {
        File downloadedFile = File.createTempFile("download", ".tmp");
        HttpResponse fileToDownload = getHTTPResponse();
        InputStream content = fileToDownload.getEntity().getContent();
        FileUtils.copyInputStreamToFile(content, downloadedFile);
        content.close();
        return downloadedFile;
    }

}
