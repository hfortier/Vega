package com.subgraph.vega.impl.scanner;

import java.net.URI;
import java.util.logging.Logger;

import com.subgraph.vega.api.crawler.ICrawlerConfig;
import com.subgraph.vega.api.crawler.ICrawlerEventHandler;
import com.subgraph.vega.api.crawler.IWebCrawler;
import com.subgraph.vega.api.crawler.IWebCrawlerFactory;
import com.subgraph.vega.api.http.requests.IHttpRequestEngine;
import com.subgraph.vega.api.http.requests.IHttpResponseProcessor;
import com.subgraph.vega.api.requestlog.IRequestLog;
import com.subgraph.vega.api.scanner.IScannerConfig;
import com.subgraph.vega.api.scanner.model.IScanDirectory;
import com.subgraph.vega.api.scanner.model.IScanHost;
import com.subgraph.vega.api.scanner.modules.IPerDirectoryScannerModule;
import com.subgraph.vega.api.scanner.modules.IPerHostScannerModule;
import com.subgraph.vega.api.scanner.modules.IScannerModuleRegistry;
import com.subgraph.vega.impl.scanner.model.ScanModel;
import com.subgraph.vega.api.model.IModel;
import com.subgraph.vega.api.model.web.IWebGetTarget;
import com.subgraph.vega.api.model.web.IWebHost;
import com.subgraph.vega.api.model.web.IWebModel;
import com.subgraph.vega.api.model.web.IWebPath;
import com.subgraph.vega.api.requestlog.IRequestLog;

public class ScannerTask implements Runnable, ICrawlerEventHandler {

	private final Logger logger = Logger.getLogger("scanner");
	private final IScannerConfig scannerConfig;
	private final ScanModel scanModel;
	private final IWebCrawlerFactory crawlerFactory;
	private final IHttpRequestEngine requestEngine;
	private final IScannerModuleRegistry moduleRegistry;
	private final IHttpResponseProcessor responseProcessor;
	private final IRequestLog requestLog;
	private final IModel model;
	
	ScannerTask(IScannerConfig config, ScanModel scanModel, IRequestLog requestLog, IModel model, IWebCrawlerFactory crawlerFactory, IHttpRequestEngine requestEngine, IScannerModuleRegistry moduleRegistry) {
		this.scannerConfig = config;
		this.scanModel = scanModel;
		this.crawlerFactory = crawlerFactory;
		this.requestEngine = requestEngine;
		this.moduleRegistry = moduleRegistry;
		this.requestLog = requestLog;
		this.model = model;
		responseProcessor = new ScannerResponseProcessor(moduleRegistry.getResponseProcessingModules(), scanModel, requestLog, model);
		this.requestEngine.registerResponseProcessor(responseProcessor);
	}
	
	@Override
	public void run() {
		scanModel.addDiscoveredURI(scannerConfig.getBaseURI());
		runCrawlerPhase();
		runPerHostModulePhase();
		runPerDirectoryModulePhase();
		logger.info("Scanner completed");
	}
	
	private void runCrawlerPhase() {
		logger.info("Starting crawling phase");
		ICrawlerConfig config = crawlerFactory.createBasicConfig(scannerConfig.getBaseURI());
		config.addEventHandler(this);
		IWebCrawler crawler = crawlerFactory.create(config, requestEngine);
		crawler.start();
		try {
			crawler.waitFinished();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Crawler finished");
	}
	
	@Override
	public void linkDiscovered(URI link) {
		scanModel.addDiscoveredURI(link);		
	}

	private void runPerHostModulePhase() {
		logger.info("Starting per host module phase");
		for(IScanHost host: scanModel.getUnscannedHosts()) {
			for(IPerHostScannerModule m: moduleRegistry.getPerHostModules()) {
				m.runScan(host, requestEngine, scanModel,model);
			}
		}
	}
	
	private void runPerDirectoryModulePhase() {
		logger.info("Starting per directory module phase");
		for(IScanDirectory dir: scanModel.getUnscannedDirectories()) {
			for(IPerDirectoryScannerModule m: moduleRegistry.getPerDirectoryModules()) {
				m.runScan(dir, requestEngine, scanModel,model);
			}
		}
	}
}
