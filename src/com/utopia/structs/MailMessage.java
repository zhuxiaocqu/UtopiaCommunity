package com.utopia.structs;

import java.io.Serializable;

public class MailMessage implements Serializable {
	private int messageKind;
	private String messageCont;

	public MailMessage(int kind, String content) {
		messageKind = kind;
		messageCont = content;
	}

	public void setKind(int kind) {
		messageKind = kind;
	}

	public void setContent(String content) {
		messageCont = content;
	}

	public int getKind() {
		return messageKind;
	}

	public String getContent() {
		return messageCont;
	}

}
