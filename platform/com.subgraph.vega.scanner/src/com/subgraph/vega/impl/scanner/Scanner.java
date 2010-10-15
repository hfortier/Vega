package com.subgraph.vega.impl.scanner;

import com.subgraph.vega.api.crawler.IWebCrawlerFactory;
import com.subgraph.vega.api.http.requests.IHttpRequestEngine;
import com.subgraph.vega.api.scanner.IScanner;
import com.subgraph.vega.api.scanner.IScannerConfig;
import com.subgraph.vega.api.scanner.modules.IScannerModuleRegistry;
import com.subgraph.vega.impl.scanner.model.ScanModel;
import com.subgraph.vega.api.model.IModel;
import com.subgraph.vega.api.model.web.IWebGetTarget;
import com.subgraph.vega.api.model.web.IWebHost;
import com.subgraph.vega.api.model.web.IWebModel;
import com.subgraph.vega.api.model.web.IWebPath;
import com.subgraph.vega.api.requestlog.IRequestLog;

public class Scanner implements IScanner {
	
	private final IScannerConfig config;
	private final IWebCrawlerFactory crawlerFactory;
	private final ScanModel scanModel;
	private final IHttpRequestEngine requestEngine;
	private final IScannerModuleRegistry moduleRegistry;
	private ScannerTask scannerTask;
	private Thread scannerThread;
	private final IModel model;
	private final IRequestLog requestLog;
	
	Scanner(IScannerConfig config, ScanModel scanModel, IRequestLog requestLog,IModel model, IWebCrawlerFactory crawlerFactory, IHttpRequestEngine requestEngine, IScannerModuleRegistry moduleRegistry) {
		this.config = config;
		this.crawlerFactory = crawlerFactory;
		this.scanModel = scanModel;
		this.requestEngine = requestEngine;
		this.moduleRegistry = moduleRegistry;
		this.requestLog = requestLog;
		this.model = model;
	}
	
	IScannerConfig getConfig() {
		return config;
	}
	
	public void start() {
		moduleRegistry.refreshModuleScripts();
		scannerTask = new ScannerTask(config, scanModel, requestLog, model, crawlerFactory, requestEngine, moduleRegistry);
		scannerThread = new Thread(scannerTask);
		scannerThread.start();
	}
}
