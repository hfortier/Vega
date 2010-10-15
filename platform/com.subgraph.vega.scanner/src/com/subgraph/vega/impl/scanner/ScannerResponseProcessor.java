package com.subgraph.vega.impl.scanner;

import java.util.List;
import com.subgraph.vega.api.requestlog.IRequestLog;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

import com.subgraph.vega.api.http.requests.IHttpResponseProcessor;
import com.subgraph.vega.api.scanner.model.IScanModel;
import com.subgraph.vega.api.scanner.modules.IResponseProcessingModule;
import com.subgraph.vega.api.model.IModel;
import com.subgraph.vega.api.model.web.IWebGetTarget;
import com.subgraph.vega.api.model.web.IWebHost;
import com.subgraph.vega.api.model.web.IWebModel;
import com.subgraph.vega.api.model.web.IWebPath;
import com.subgraph.vega.api.requestlog.IRequestLog;

public class ScannerResponseProcessor implements IHttpResponseProcessor {
	private final List<IResponseProcessingModule> responseProcessingModules;
	private final IScanModel scanModel;
	private final IRequestLog requestLog;
	private final IModel model;
	
	public ScannerResponseProcessor(List<IResponseProcessingModule> responseProcessingModules, IScanModel scanModel, IRequestLog requestLog, IModel model) {
		this.responseProcessingModules = responseProcessingModules;
		this.scanModel = scanModel;
		this.model = model;
		this.requestLog = requestLog;
	}
	
	@Override
	public void processResponse(HttpRequest request, HttpResponse response, HttpContext context) {
		if(responseProcessingModules.isEmpty())
			return;
		final int statusCode = response.getStatusLine().getStatusCode();
		final String mimeType = responseToMimeType(response);
		
		for(IResponseProcessingModule m: responseProcessingModules) {
			if(m.responseCodeFilter(statusCode) && m.mimeTypeFilter(mimeType)) 
				m.processResponse(request, response, scanModel,requestLog,model);
		}
	}
	
	private String responseToMimeType(HttpResponse response) {
		final HttpEntity entity = response.getEntity();
		if(entity == null)
			return null;
		final Header contentType = entity.getContentType();
		if(contentType == null)
			return null;
		return contentType.getValue();
	}

}
