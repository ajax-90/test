package com.engagepoint.acceptancetest.base.webservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;

import static com.engagepoint.acceptancetest.base.utils.FileUtils.getStreamFromResourcesByFilePath;

public final class WebServiceHelper {
	
	private WebServiceHelper() {}

	private static final String SOAP_ACTION_PROPERTY_NAME = "SOAPAction";
	private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceHelper.class);

	public static SOAPMessage callWebServiceMethod(SOAPMessage request,	Object address) throws SOAPException, KeyManagementException, NoSuchAlgorithmException {
		doTrustToCertificates();
		SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection connection = connectionFactory.createConnection();
		SOAPMessage response = connection.call(request, address);
		if (response.getSOAPBody().hasFault()) {
			parseFault(response);
		}
		connection.close();
		return response;
	}
	
	public static void parseFault(SOAPMessage message) throws SOAPException {
		SOAPFault fault = message.getSOAPBody().getFault();
		LOGGER.info("fault code = " + fault.getFaultCode());
		LOGGER.info("fault actor = " + fault.getFaultActor());
		LOGGER.info("fault string = " + fault.getFaultString());
		LOGGER.info("detail = " + fault.getDetail());
	}
	
	public static SOAPMessage createSOAPMessageFromFile(String file, String webServiceSoapAction) throws SOAPException, FileNotFoundException {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage message = messageFactory.createMessage();
		SOAPPart soapPart = message.getSOAPPart();
		MimeHeaders headers = message.getMimeHeaders();
		headers.addHeader(SOAP_ACTION_PROPERTY_NAME, webServiceSoapAction);
        StreamSource preppedMsgSrc = new StreamSource(getStreamFromResourcesByFilePath(file));
		soapPart.setContent(preppedMsgSrc);
		message.saveChanges();
		return message;
	}
	
	public static String getXmlMessage(SOAPMessage message) throws SOAPException, IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		message.writeTo(os);
		String encodingString = "UTF-8";
		Object encoding = message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING);
		if (encoding instanceof String){
			encodingString = (String) encoding;
		}
		return new String(os.toByteArray(), encodingString);
	}
	
	@SuppressWarnings("restriction")
	private static void doTrustToCertificates() throws KeyManagementException, NoSuchAlgorithmException {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new TrustAllTrustManager();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(getHostNameVerifier());
    }
	
	private static HostnameVerifier getHostNameVerifier() {
		return new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                String peerHost = session.getPeerHost();
                if (!urlHostName.equalsIgnoreCase(peerHost)) {
                	LOGGER.warn("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + peerHost + "'.");
                }
                return true;
            }
        };
	}
	
	private static class TrustAllTrustManager implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[]{};
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
        }

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
        }
	}
}