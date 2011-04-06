package com.subgraph.vega.internal.model.conditions;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import com.subgraph.vega.api.model.conditions.IHttpCondition;
import com.subgraph.vega.api.model.conditions.IHttpConditionType;
import com.subgraph.vega.api.model.conditions.IHttpConditionType.HttpConditionStyle;

public class ConditionHeader extends AbstractRegexCondition {

	static IHttpConditionType createRequestType() {
		return new ConditionType("request header", HttpConditionStyle.CONDITION_REGEX) {			
			@Override
			public IHttpCondition createConditionInstance() {
				return new ConditionHeader(true);
			}
		};
	}
	
	static IHttpConditionType createResponseType() {
		return new ConditionType("response header", HttpConditionStyle.CONDITION_REGEX) {			
			@Override
			public IHttpCondition createConditionInstance() {
				return new ConditionHeader(false);
			}
		};
	}

	private final boolean matchRequestHeader;
	
	ConditionHeader(boolean matchRequestHeader) {
		this.matchRequestHeader = matchRequestHeader;
	}
	
	@Override
	public boolean matches(HttpRequest request) {
		if(matchRequestHeader)
			return maybeInvert(hasMatchingHeader(request));
		else
			return false;
	}

	@Override
	public boolean matches(HttpResponse response) {
		if(!matchRequestHeader)
			return maybeInvert(hasMatchingHeader(response));
		else
			return false;
	}

	private boolean hasMatchingHeader(HttpMessage message) {
		if(message == null)
			return false;
		
		for(Header h: message.getAllHeaders()) {
			if(matchesPattern(h.toString()))
				return true;
		}
		return false;
	}
	
	@Override
	public boolean matches(HttpRequest request, HttpResponse response) {
		if(matchRequestHeader)
			return maybeInvert(hasMatchingHeader(request));
		else
			return maybeInvert(hasMatchingHeader(response));
	}

	@Override
	public IHttpConditionType getType() {
		if(matchRequestHeader)
			return createRequestType();
		else
			return createResponseType();
	}
}
