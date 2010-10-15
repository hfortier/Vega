package com.subgraph.vega.impl.scanner.modules.scripting;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import com.subgraph.vega.api.scanner.model.IScanModel;
import com.subgraph.vega.api.scanner.modules.IResponseProcessingModule;
import com.subgraph.vega.api.model.IModel;
import com.subgraph.vega.api.model.web.IWebGetTarget;
import com.subgraph.vega.api.model.web.IWebHost;
import com.subgraph.vega.api.model.web.IWebModel;
import com.subgraph.vega.api.model.web.IWebPath;
import com.subgraph.vega.api.requestlog.IRequestLog;


public class ResponseProcessorScript extends AbstractScriptModule implements IResponseProcessingModule {

	public ResponseProcessorScript(ScriptedModule module) {
		super(module);
	}
	
	public void processResponse(HttpRequest request, IHttpResponse response,
			IScanModel scanModel,IRequestLog requestLog,IModel model) {
		IWebModel webModel;
		webModel = model.getCurrentWorkspace().getWebModel();
		export("httpRequest", request);
		export("httpResponse", response);
		export("scanModel", scanModel);
		export("requestLog", requestLog);
		export("webModel", webModel);
		runScript();
	}

	@Override
	public boolean responseCodeFilter(int code) {
		return true;
	}

	@Override
	public boolean mimeTypeFilter(String mimeType) {
		return true;
	}

}
