package com.engagepoint.acceptancetest.base.props;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import com.google.inject.Inject;

public class CustomSystemProperties {
	
	private static CustomSystemProperties customSystemProperties = new CustomSystemProperties();
	
	public static CustomSystemProperties getCustomSystemProperties() {
		return customSystemProperties;
	}

	private final EnvironmentVariables environmentVariables;
	
	public CustomSystemProperties() {
		this(Injectors.getInjector().getInstance(EnvironmentVariables.class));
	}

    @Inject
    public CustomSystemProperties(EnvironmentVariables environmentVariables) {
        this.environmentVariables = environmentVariables;
    }
    
    private boolean isDefined(String name) {
        return !isEmpty(environmentVariables.getProperty(name));
    }
    
    public String getPropertyWithName(String name) {
    	if (isDefined(name)) {
			return environmentVariables.getProperty(name);
		} else {
            return "";
        }
    }
    
	public String getHostPartFromBaseUrl() {
		Pattern p = Pattern.compile("https?://[^/]+");
		Matcher m = p.matcher(environmentVariables.getProperty(ThucydidesSystemProperty.BASE_URL));
		if(m.find()) {
			return m.group();
		}
		return "";
	}
    
}
