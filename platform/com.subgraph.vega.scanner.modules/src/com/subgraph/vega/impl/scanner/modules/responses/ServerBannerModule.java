package com.subgraph.vega.impl.scanner.modules.responses;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import com.subgraph.vega.api.scanner.model.IScanAlert;
import com.subgraph.vega.api.scanner.model.IScanModel;
import com.subgraph.vega.api.scanner.modules.AbstractResponseProcessingModule;
import com.subgraph.vega.api.model.IModel;
import com.subgraph.vega.api.model.web.IWebGetTarget;
import com.subgraph.vega.api.model.web.IWebHost;
import com.subgraph.vega.api.model.web.IWebModel;
import com.subgraph.vega.api.model.web.IWebPath;
import com.subgraph.vega.api.requestlog.IRequestLog;


public class ServerBannerModule extends AbstractResponseProcessingModule {

	@Override
	public void processResponse(HttpRequest request, IHttpResponse response, IScanModel scanModel,IRequestLog requestLog, IModel model) {
		final Header serverHeader = response.getRawResponse().getFirstHeader("Server");
		
		if(serverHeader == null || serverHeader.getValue() == null) 
			return;
		
		final String requestBanner = serverHeader.getValue();
		final String existingBanner = scanModel.getStringProperty("server.banner");
		
		if(existingBanner == null) {
			final Header hostHeader = response.getFirstHeader("Host");
			final String host = (hostHeader != null) ? (hostHeader.getValue()) : (null);
			scanModel.setStringProperty("server.banner", requestBanner);
			final IScanAlert alert = scanModel.createAlert("banner");
			alert.setProperty("output", requestBanner);
			if(host != null)
				alert.setProperty("resource", host);
			scanModel.addAlert(alert);
			return;
		} else if(!existingBanner.equals(requestBanner)) {
			// XXX report some alert here
		}
	}

}
