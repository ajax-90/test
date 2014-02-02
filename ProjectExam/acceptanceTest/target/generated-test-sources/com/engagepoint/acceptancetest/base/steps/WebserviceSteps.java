package com.engagepoint.acceptancetest.base.steps;

import static com.engagepoint.acceptancetest.base.utils.FileUtils.getFileFromResourcesByFilePath;
import static com.engagepoint.acceptancetest.base.webservice.utils.WebServiceHelper.callWebServiceMethod;
import static com.engagepoint.acceptancetest.base.webservice.utils.WebServiceHelper.createSOAPMessageFromFile;
import static com.engagepoint.acceptancetest.base.webservice.utils.WebServiceHelper.getXmlMessage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import net.thucydides.core.Thucydides;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.IgnoreTextAndAttributeValuesDifferenceListener;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class WebserviceSteps extends ScenarioSteps {
	
	private static final long serialVersionUID = 1L;
	private static final String FAULT_STRING = "fault string";
	private static final String FAULT_ACTOR = "fault actor";
	private static final String FAULT_CODE = "fault code";
	private static final Logger LOGGER = LoggerFactory.getLogger(WebserviceSteps.class);
	private static final String HTTPS_PROTOCOL = "https";
	private static final String HTTP_PROTOCOL = "http";
	private static final String WEB_SERVICE_SOAP_ACTION_KEY = "webServiceSoapAction";
	private static final String WEB_SERVICE_ENDPOINT_URL_KEY = "webServicesEndpointUrl";

	public WebserviceSteps(Pages pages) {
		super(pages);
	}

	private transient SOAPMessage lastResponse;

	@Given("web service endpoint '$webServiceEndpointUrl'")
	public void givenWebServiceEndpoint(String webServiceEndpointUrl) {
		Thucydides.getCurrentSession().put(WEB_SERVICE_ENDPOINT_URL_KEY, webServiceEndpointUrl);
	}
	
	@Given("published service endpoint '$serviceSuffix' with port '$port' and protocol '$protocol'")
	public void givenWebServiceEndpointWith(String serviceSuffix, String port, String protocol) throws MalformedURLException {
		String baseUrl = pages().getConfiguration().getBaseUrl();
		savePublishedEndpoint(protocol, baseUrl, port, serviceSuffix);
	}

	public void savePublishedEndpoint(String protocol, String host, String port, String serviceSuffix) throws MalformedURLException {
		URL hostUrl = new URL(host);
		String endpointProtocol = HTTP_PROTOCOL;
		if(HTTPS_PROTOCOL.equalsIgnoreCase(protocol)){
			endpointProtocol = HTTPS_PROTOCOL;
		} 
		String webServiceEndpointUrl = endpointProtocol + "://" + hostUrl.getHost() + ":" + port + "/" + serviceSuffix;
		Thucydides.getCurrentSession().put(WEB_SERVICE_ENDPOINT_URL_KEY, webServiceEndpointUrl);
		LOGGER.info("Saved published endpoint URL is:" + webServiceEndpointUrl);
	}
	
	@Given("web service SOAP Action '$webServiceSoapAction'")
	public void givenWebServiceURL(String webServiceSoapAction) {
		Thucydides.getCurrentSession().put(WEB_SERVICE_SOAP_ACTION_KEY, webServiceSoapAction);
	}
	
	@When("send SOAP msg from file '$filePath'")
	public void whenSendSOAPMsgFromFile(String filePath) throws FileNotFoundException, SOAPException, KeyManagementException, NoSuchAlgorithmException {
		Map<String, String> currentSession = Thucydides.getCurrentSession();
		String webServicesEndpointUrl = currentSession.get(WEB_SERVICE_ENDPOINT_URL_KEY);
		String webServiceSoapAction = currentSession.get(WEB_SERVICE_SOAP_ACTION_KEY);
		SOAPMessage msg = createSOAPMessageFromFile(filePath, webServiceSoapAction);
		lastResponse = callWebServiceMethod(msg, webServicesEndpointUrl);
	}

	@Then("verify that response is equal to file '$filePath'")
	public void thenVerifyThatResponseIsEqualToFile(String filePath) throws SOAPException, IOException, SAXException{
        FileReader controlXML = new FileReader(getFileFromResourcesByFilePath(filePath));
		String resultStr = getXmlMessage(lastResponse);
		StringReader testXML = new StringReader(resultStr);
		XMLUnit.setIgnoreWhitespace(true);
		XMLAssert.assertXMLEqual(controlXML, testXML);
	}

	@Then("verify that response is similar to file '$filePath'")
	public void thenVerifyThatResponseIsSimilarToFile(String filePath) throws SOAPException, IOException, SAXException{
        FileReader controlXML = new FileReader(getFileFromResourcesByFilePath(filePath));
		String resultStr = getXmlMessage(lastResponse);
		StringReader testXML = new StringReader(resultStr);
		Diff diff = new Diff(controlXML, testXML);
		diff.overrideDifferenceListener(new IgnoreTextAndAttributeValuesDifferenceListener());
		XMLAssert.assertXMLEqual(diff, true);
	}
	
	@Then("verify that '$faultType' is equal to '$value'")
	public void thenVerifyThatFaultCodeIsEqualTo(String faultType, String value) throws SOAPException {
		SOAPFault fault = lastResponse.getSOAPBody().getFault();
		if (FAULT_CODE.equalsIgnoreCase(faultType)) {
			Assert.assertEquals(value, fault.getFaultCode());
		} else if (FAULT_ACTOR.equalsIgnoreCase(faultType)) {
			Assert.assertEquals(value, fault.getFaultActor());
		} else if (FAULT_STRING.equalsIgnoreCase(faultType)) {
			Assert.assertEquals(value, fault.getFaultString());
		} else {
			throw new AssertionError("Please enter valid fault type such as: "
					+ FAULT_CODE + ", " + FAULT_ACTOR + ", " + FAULT_STRING);
		}
	}

}
