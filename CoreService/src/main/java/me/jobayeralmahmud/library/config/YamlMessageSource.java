package me.jobayeralmahmud.library.config;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// @Component("messageSource")
public class YamlMessageSource extends AbstractResourceBasedMessageSource {

    private final ConcurrentMap<String, Properties> cachedProperties = new ConcurrentHashMap<>();

    public YamlMessageSource() {
        setDefaultEncoding("UTF-8");
    }

    @Override
    protected MessageFormat resolveCode(@NonNull String code, @NonNull Locale locale) {
        String msg = getProperty(code, locale);
        if (msg == null) {
            return null;
        }
        return createMessageFormat(msg, locale);
    }

    @Override
    protected String resolveCodeWithoutArguments(@NonNull String code, @NonNull Locale locale) {
        return getProperty(code, locale);
    }

    private String getProperty(String code, Locale locale) {
        Properties properties = getPropertiesForLocale(locale);
        return properties.getProperty(code);
    }

    private Properties getPropertiesForLocale(Locale locale) {
        String filename = getFilename(locale);
        return cachedProperties.computeIfAbsent(filename, this::loadYamlProperties);
    }

    private String getFilename(Locale locale) {
        String lang = locale.getLanguage();
        // Generates messages_en.yaml or messages.yaml if default
        if (!lang.isEmpty()) {
            return "messages_" + lang + ".yaml";
        }
        return "messages.yaml";
    }

    private Properties loadYamlProperties(String filename) {
        Resource resource = new ClassPathResource(filename);
        
        // If the file with current extension doesn't exist, try the alternative extension (.yml vs .yaml)
        if (!resource.exists()) {
            String altFilename = filename.endsWith(".yaml") 
                    ? filename.substring(0, filename.length() - 5) + ".yml"
                    : filename.substring(0, filename.length() - 4) + ".yaml";
            resource = new ClassPathResource(altFilename);
        }

        // If the locale-specific file is still missing, fallback to default messages.yaml or messages.yml
        if (!resource.exists()) {
            resource = new ClassPathResource("messages.yaml");
            if (!resource.exists()) {
                resource = new ClassPathResource("messages.yml");
            }
        }

        if (!resource.exists()) {
            return new Properties();
        }

        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);
        return factory.getObject();
    }
}