package com.subgraph.vega.internal.crawler;

import java.io.InterruptedIOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.methods.HttpUriRequest;

import com.subgraph.vega.api.http.requests.IHttpRequestEngine;
import com.subgraph.vega.api.http.requests.IHttpResponse;
import com.subgraph.vega.api.http.requests.RequestEngineException;

public class RequestConsumer implements Runnable {
	private final Logger logger = Logger.getLogger("crawler");
	private final IHttpRequestEngine requestEngine;
	private final BlockingQueue<CrawlerTask> crawlerRequestQueue;
	private final BlockingQueue<CrawlerTask> crawlerResponseQueue;
	private final CountDownLatch latch;
	private volatile boolean stop;
	private final Object requestLock = new Object();
	private volatile HttpUriRequest activeRequest = null;

	RequestConsumer(IHttpRequestEngine requestEngine, BlockingQueue<CrawlerTask> requestQueue, BlockingQueue<CrawlerTask> responseQueue, CountDownLatch latch) {
		this.requestEngine = requestEngine;
		this.crawlerRequestQueue = requestQueue;
		this.crawlerResponseQueue = responseQueue;
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			runLoop();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			latch.countDown();
		}		
	}
	
	void stop() {
		stop = true;
		synchronized (requestLock) {
			if(activeRequest != null)
				activeRequest.abort();
		}
	}
	
	private void runLoop() throws InterruptedException {
		while(!stop) {
			CrawlerTask task = (CrawlerTask) crawlerRequestQueue.take();
			
			if(task.isExitTask()) {
				// Put poison pill back in queue so every other RequestConsumer task will see it.
				crawlerRequestQueue.add(task);
				return;
			}

			logger.info("Retrieving: " + task.getRequest().getRequestLine().getUri());

			if(!sendRequest(task)) {
				if(!stop && !task.causedException()) {
					logger.log(Level.WARNING, "No response was receiven for request to "+ task.getRequest().getURI());					
				}
			}
			crawlerResponseQueue.put(task);
		}
	}

	private boolean sendRequest(CrawlerTask task) {
		try {
			activeRequest = task.getRequest();
			final IHttpResponse response = requestEngine.sendRequest(task.getRequest());
			task.setResponse(response);
			return response != null;
		} catch (RequestEngineException e) {
			if(e.getCause() instanceof InterruptedIOException) {
				stop = true;
				return false;
			}
			if(e.getMessage().contains("abort")) {
				return false;
			}
			task.setException(e);
			logger.log(Level.WARNING, e.getMessage());
			return false;
		} finally {
			synchronized(requestLock) {
				activeRequest = null;
			}
		}
	}
}
