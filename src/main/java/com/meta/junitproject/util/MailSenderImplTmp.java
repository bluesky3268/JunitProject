package com.meta.junitproject.util;

import org.springframework.stereotype.Component;

//@Component
public class MailSenderImplTmp implements MailSender{

    @Override
    public boolean send() {
        return true;
    }
}
