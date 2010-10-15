package com.subgraph.vega.impl.scanner;

import com.subgraph.vega.api.crawler.IWebCrawlerFactory;
import com.subgraph.vega.api.http.requests.IHttpRequestEngine;
import com.subgraph.vega.api.http.requests.IHttpRequestEngineFactory;
import com.subgraph.vega.api.scanner.IScanner;
import com.subgraph.vega.api.scanner.IScannerConfig;
import com.subgraph.vega.api.scanner.IScannerFactory;
import com.subgraph.vega.api.scanner.model.IScanAlertRepository;
import com.subgraph.vega.api.scanner.model.IScanModel;
import com.subgraph.vega.api.scanner.modules.IScannerModuleRegistry;
import com.subgraph.vega.impl.scanner.model.ScanModel;
import com.subgraph.vega.api.model.IModel;
import com.subgraph.vega.api.model.web.IWebGetTarget;
import com.subgraph.vega.api.model.web.IWebHost;
import com.subgraph.vega.api.model.web.IWebModel;
import com.subgraph.vega.api.model.web.IWebPath;
import com.subgraph.vega.api.requestlog.IRequestLog;

public class ScannerFactory implements IScannerFactory {

	private ScanModel scanModel;
	
	private IWebCrawlerFactory crawlerFactory;
	private IHttpRequestEngineFactory requestEngineFactory;
	private IScannerModuleRegistry moduleRegistry;
	private IScanAlertRepository scanAlertRepository;
	private IRequestLog requestLog;
	private IModel model;
	
	protected void activate() {
		scanModel = new ScanModel(scanAlertRepository);
	}
	
	protected void deactivate() {
		
	}
	@Override
	public IScanModel getScanModel() {
		return scanModel;
	}

	@Override
	public IScannerConfig createScannerConfig() {
		return new ScannerConfig();
	}

	@Override
	public IScanner createScanner(IScannerConfig config) {
		final IHttpRequestEngine requestEngine = requestEngineFactory.createRequestEngine(requestEngineFactory.createConfig());
		return new Scanner(config, scanModel, requestLog, model, crawlerFactory, requestEngine, moduleRegistry);
	}

	protected void setRequestLog(IRequestLog requestLog) {
		this.requestLog = requestLog;
	}
	
	protected void unsetRequestLog(IRequestLog requestLog) {
		this.requestLog = null;
	}

	protected void setModel(IModel model) {
		this.model = model;
	}
	
	protected void unsetModel(IModel model) {
		this.model = null;
	}
	
	protected void setCrawlerFactory(IWebCrawlerFactory crawlerFactory) {
		this.crawlerFactory = crawlerFactory;
	}
	
	protected void unsetCrawlerFactory(IWebCrawlerFactory crawlerFactory) {
		this.crawlerFactory = null;
	}
	
	protected void setRequestEngineFactory(IHttpRequestEngineFactory factory) {
		this.requestEngineFactory = factory;
	}
	
	protected void unsetRequestEngineFactory(IHttpRequestEngineFactory factory) {
		this.requestEngineFactory = null;
	}
	
	protected void setModuleRegistry(IScannerModuleRegistry registry) {
		this.moduleRegistry = registry;
	}
	
	protected void unsetModuleRegistry(IScannerModuleRegistry registry) {
		this.moduleRegistry = null;
	}
	
	protected void setScanAlertRepository(IScanAlertRepository repo) {
		this.scanAlertRepository = repo;
		
	}
	
	protected void unsetScanAlertRepository(IScanAlertRepository repo) {
		this.scanAlertRepository = null;
	}
}
