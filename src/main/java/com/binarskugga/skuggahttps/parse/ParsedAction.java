package com.binarskugga.skuggahttps.parse;

import java.lang.reflect.*;
import java.util.*;

public class ParsedAction {
	private Method action;
	private Map<Integer, Map.Entry<URLParamParser, String>> params;
	private String body;

	public ParsedAction() {
		this.params = new HashMap<>();
	}

	public void addParam(int index, URLParamParser parser, String value) {
		this.params.put(index, new AbstractMap.SimpleEntry<>(parser, value));
	}

	public void setParam(int index, String value) {
		this.params.get(index).setValue(value);
	}

	public void setAction(Method action) {
		this.action = action;
	}

	public Method getAction() {
		return this.action;
	}

	public List<Object> args() {
		List<Object> args = new ArrayList<>(params.size() + 2);
		for(int i = 0; i < params.size(); i++)
			args.add(params.get(i).getKey().parse(params.get(i).getValue()));
		return args;
	}


}
