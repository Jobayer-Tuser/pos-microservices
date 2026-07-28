package me.jobayeralmahmud.library.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Messages {

    private static MessageSource messageSource;

    public Messages(MessageSource messageSource) {
        Messages.messageSource = messageSource;
    }

    public static String get(String key) {
        return messageSource.getMessage(key, null, key, LocaleContextHolder.getLocale());
    }
}