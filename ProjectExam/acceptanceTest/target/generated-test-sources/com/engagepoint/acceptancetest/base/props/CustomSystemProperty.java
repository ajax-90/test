package com.engagepoint.acceptancetest.base.props;

public enum CustomSystemProperty {
	
	/**
     * Set SMTP host of test mail box
     */
	TEST_SMTP_HOST,
	
	/**
     * Set SMTP login of test mail box
     */
	TEST_SMTP_SERVER_LOGIN,
	
	/**
     * Set SMTP password of test mail box
     */
	TEST_SMTP_SERVER_PASSWORD,
	
	/**
     * Set POP3 host of test mail box
     */
	TEST_POP3_HOST,
	
	/**
     * Set login of test mail box
     */
	TEST_MAILBOX_LOGIN,
	/**
     * Set password of test mail box
     */
	TEST_MAILBOX_PASSWORD;

	public String getName() {return toString().toLowerCase().replaceAll("_",".");}
}
