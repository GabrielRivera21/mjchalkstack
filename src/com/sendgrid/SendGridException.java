package com.sendgrid;

@SuppressWarnings("serial")
public class SendGridException extends Exception {
  public SendGridException(Exception e) {
    super(e);
  }
}
