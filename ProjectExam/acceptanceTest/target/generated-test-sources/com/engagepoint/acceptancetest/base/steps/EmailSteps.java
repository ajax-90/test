package com.engagepoint.acceptancetest.base.steps;

import com.engagepoint.acceptancetest.base.email.EmailHelper;
import com.engagepoint.acceptancetest.base.email.MessageBean;
import com.engagepoint.acceptancetest.base.props.CustomSystemProperties;
import com.engagepoint.acceptancetest.base.props.CustomSystemProperty;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import javax.mail.MessagingException;
import java.io.IOException;

import static com.engagepoint.acceptancetest.base.props.CustomSystemProperties.getCustomSystemProperties;

public class EmailSteps extends ScenarioSteps{
	
	private static final long serialVersionUID = 1L;

	public EmailSteps(Pages pages) {
		super(pages);
	}
	
	@When("'$from' send email to test mailbox with subject '$subject' and body '$body'")
	public void whenUserSendEmailToTestMailboxWithSubjectAndBody(String from, String subject, String body) throws MessagingException {
		CustomSystemProperties customProperties = getCustomSystemProperties();
		String login = customProperties.getPropertyWithName(CustomSystemProperty.TEST_SMTP_SERVER_LOGIN.getName());
		String password = customProperties.getPropertyWithName(CustomSystemProperty.TEST_SMTP_SERVER_PASSWORD.getName());
		String to = customProperties.getPropertyWithName(CustomSystemProperty.TEST_MAILBOX_LOGIN.getName());
		EmailHelper.sendSimpleMessage(login, password, from, to, subject, body);
	}

	@Then("verify that letter sent to test mailbox with subject '$subject' and body '$body'")
	public void thenVerifyThatLetterSentToTestMailboxWithSubjectAndBody(String subject, String body) throws MessagingException, IOException {
		CustomSystemProperties customProperties = getCustomSystemProperties();
		String mailUser = customProperties.getPropertyWithName(CustomSystemProperty.TEST_MAILBOX_LOGIN.getName());
		String mailPassword = customProperties.getPropertyWithName(CustomSystemProperty.TEST_MAILBOX_PASSWORD.getName());
		waitFor(pages().getConfiguration().getElementTimeout()).seconds();
		MessageBean lastMessage = EmailHelper.getLastNotSeenMessageContent(mailUser, mailPassword);
		Assert.assertEquals(subject, lastMessage.getSubject());
		Assert.assertEquals(body, lastMessage.getContent());
	}
}
