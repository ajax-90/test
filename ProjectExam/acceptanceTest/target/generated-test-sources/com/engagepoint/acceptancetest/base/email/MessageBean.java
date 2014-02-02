package com.engagepoint.acceptancetest.base.email;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class MessageBean implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String subject;
    private String from;
    private String to;
    private String dateSent;
    private String content;
    private boolean isNew;
    private int msgId;
    private List<File> attachments;

    public MessageBean(int msgId, String subject, String from, String to, String dateSent, String content, boolean isNew, List<File> attachments) {
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.dateSent = dateSent;
        this.content = content;
        this.isNew = isNew;
        this.msgId = msgId;
        this.attachments = attachments;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<File> attachments) {
        this.attachments = attachments;
    }
}
