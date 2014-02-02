package com.engagepoint.acceptancetest.base.filedownload.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public final class RequestParameters {

    public static final String NAME_ATTRIBUTE = "name";

    private RequestParameters(){}

    public static List<NameValuePair> getParametersFrom(WebElement element, String attribute) {
        List<NameValuePair> parameters = Lists.newArrayList();
        String valueAttr = element.getAttribute(attribute);
        String[] paramsString = {"", ""};
        if(NAME_ATTRIBUTE.equals(attribute)){
            paramsString[0] = valueAttr;
        } else {
            paramsString = StringUtils.substringBetween(valueAttr, "{'", "'}").split("':'");
        }
        for(int i=0; i < paramsString.length; i=i+2){
            parameters.add(new BasicNameValuePair(getStringSafe(paramsString[i]), getStringSafe(paramsString[i+1])));
        }
        return parameters;
    }

    private static String getStringSafe(String s) {
        return s == null ? "" : s;
    }

    public static List<NameValuePair> getParametersFrom(WebElement form){
        List<NameValuePair> parameters = Lists.newArrayList();
        List<WebElement> allInputs = form.findElements(By.tagName("input"));
        for(WebElement input : allInputs){
            String name = input.getAttribute("name");
            String value = input.getAttribute("value");
            if(!name.isEmpty()){
                parameters.add(new BasicNameValuePair(name, getStringSafe(value)));
            }
        }
        return parameters;
    }
}