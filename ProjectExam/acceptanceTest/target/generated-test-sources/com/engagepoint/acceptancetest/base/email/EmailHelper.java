package com.engagepoint.acceptancetest.base.email;

import static com.engagepoint.acceptancetest.base.props.CustomSystemProperties.getCustomSystemProperties;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.engagepoint.acceptancetest.base.props.CustomSystemProperty;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;

public final class EmailHelper {

	private EmailHelper() {}
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailHelper.class);
	
	public static void sendSimpleMessage(String login, String password,
			String from, String to, String subject, String content)
			throws MessagingException {
		Session session = Session.getInstance(getSmtpSslProperties());
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		msg.setSubject(subject);
		msg.setText(content);
		Transport t = session.getTransport("smtp");
	    try {
	        t.connect(login, password);
	        t.sendMessage(msg, msg.getAllRecipients());
	    } finally {
	        t.close();
	    }
		LOGGER.info("Email was sent to: " + to);
	}
	
	public static MessageBean getLastNotSeenMessageContent(String user, String password) throws MessagingException, IOException {
	    Session emailSession = Session.getInstance(getPop3SslProperties());
        POP3Store emailStore = (POP3Store) emailSession.getStore("pop3");
		emailStore.connect(user, password);
		POP3Folder inbox = (POP3Folder) emailStore.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
        FetchProfile profile = new FetchProfile();
        profile.add(UIDFolder.FetchProfileItem.UID);
        Message[] messages = inbox.getMessages();
        inbox.fetch(messages, profile);
        Message lastMsg = inbox.getMessage(getNumberOfLastRecivedMessage(inbox, messages));
        MessageBean lastMessagePart = getPart(lastMsg);
        inbox.close(false);
        emailStore.close();
        LOGGER.info("Email was received from: " + lastMessagePart.getFrom());
		return lastMessagePart;
    }
	
	private static MessageBean getPart(Message message)
			throws MessagingException, IOException {
		if (message.isMimeType("text/*")) {
			return new MessageBean(message.getMessageNumber(),
					MimeUtility.decodeText(message.getSubject()),
					message.getFrom()[0].toString(), "", "",
					StringUtils.chomp((String) message.getContent()), false,
					null);
		} else if (message.isMimeType("multipart/*")) {
			return getMessageBeanFromMuiltiPartMsg(message);
		} else {
			throw new MessagingException();
		}
	}
        
	private static MessageBean getMessageBeanFromMuiltiPartMsg(Message message)
			throws IOException, MessagingException {
		ArrayList<File> attachments = new ArrayList<File>();
		StringBuilder contentBuilder = new StringBuilder();
		Multipart multipart = (Multipart) message.getContent();
		for (int i = 0, n = multipart.getCount(); i < n; i++) {
			Part part = multipart.getBodyPart(i);
			String disposition = part.getDisposition();
			if ((StringUtils.isNotEmpty(disposition))
					&& ((Part.ATTACHMENT.contentEquals(disposition) || Part.INLINE
							.contentEquals(disposition)))) {
				attachments.add(saveFile(part.getFileName(),
						part.getInputStream()));
			} else {
				contentBuilder.append(part.getContent().toString());
			}
		}
		return new MessageBean(message.getMessageNumber(),
				MimeUtility.decodeText(message.getSubject()),
				message.getFrom()[0].toString(), "", "",
				StringUtils.chomp(contentBuilder.toString()), false, attachments);
	}
        
	private static File saveFile(String filename, InputStream inputStream) {
		File file = new File(filename);
		for (int i = 0; file.exists(); i++) {
			file = new File(filename + i);
		}
		try {
			IOUtils.copy(inputStream, new FileWriter(file), Charsets.UTF_8);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		return file;
	}

	private static int getNumberOfLastRecivedMessage(POP3Folder inbox, Message... messages) throws MessagingException {
		int lastMsgNumber = 1;
        int previousId = 0;
		for (int i = 0; i < messages.length; i++) {
			String uid = inbox.getUID(messages[i]);
			int id = Integer.parseInt(StringUtils.substringBetween(uid, "UID", "-"));
			if(id > previousId) {
				lastMsgNumber = i+1;
			}
			previousId = id;
		}
		return lastMsgNumber;
	}

	private static Properties getSmtpSslProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.ssl.enable", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.socketFactory.port", "25");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.port", "25");
		properties.setProperty("mail.smtp.host", getCustomSystemProperties().getPropertyWithName(CustomSystemProperty.TEST_SMTP_HOST.getName()));
		return properties;
	}
	
	private static Properties getPop3SslProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		properties.setProperty("mail.pop3.port", "995");
		properties.setProperty("mail.pop3.socketFactory.port", "995");
		properties.setProperty("mail.pop3.host", getCustomSystemProperties().getPropertyWithName(CustomSystemProperty.TEST_POP3_HOST.getName()));
		return properties;
	}
	
}
