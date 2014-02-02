package com.engagepoint.acceptancetest.base.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.thucydides.core.Thucydides;

public final class TestSessionUtils {
	

	private TestSessionUtils() {}

	public static String getVariableFromTestSessionIfExist(String variableKey) {
		String variable = (String) Thucydides.getCurrentSession().get(variableKey);
		return variable != null ? variable : variableKey;
	}
	
	public static String replaceMatcherByVariable(String input) {
		String out = input;
		Pattern p = Pattern.compile("#\\{[^}]*\\}");
		Matcher m = p.matcher(input);
		Set<String> keys = new HashSet<String>();
		while(m.find()) {
			keys.add(m.group());
		}
		for(String key : keys) {
			String variableKey = key.substring(2, key.length() - 1);
			CharSequence variable = getVariableFromTestSessionIfExist(variableKey);
			out = input.replace(key, variable);
		}
		return keys.isEmpty() ? getVariableFromTestSessionIfExist(out) : out;
	}
}
