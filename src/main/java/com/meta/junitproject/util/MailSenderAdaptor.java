package com.meta.junitproject.util;

import org.springframework.stereotype.Component;

//@Component
public class MailSenderAdaptor implements MailSender{

    private Mail mail;

    public MailSenderAdaptor() {
        this.mail = new Mail();
    }

    @Override
    public boolean send() {
//        mail 클래스에서 메일을 보내는 메서드 호출
        return true;
    }
}
