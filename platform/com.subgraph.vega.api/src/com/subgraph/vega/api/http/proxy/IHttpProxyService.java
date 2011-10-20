/*******************************************************************************
 * Copyright (c) 2011 Subgraph.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Subgraph - initial API and implementation
 ******************************************************************************/
package com.subgraph.vega.api.http.proxy;

import java.util.List;

import com.subgraph.vega.api.scanner.modules.IResponseProcessingModule;

public interface IHttpProxyService {
	void registerEventHandler(IHttpProxyServiceEventHandler handler);
	void unregisterEventHandler(IHttpProxyServiceEventHandler handler);
	boolean isRunning();
	boolean isPassthrough();
	IHttpProxyListenerConfig createListenerConfig();
	void setListenerConfigs(IHttpProxyListenerConfig[] listenerConfigs);
	IHttpProxyListenerConfig[] getListenerConfigs();
	int getListenerConfigsCount();
	void setPassthrough(boolean enabled);
	void start();
	void stop();
	IHttpProxyTransactionManipulator getTransactionManipulator();
	IHttpInterceptor getInterceptor();
	List<IResponseProcessingModule> getResponseProcessingModules();
}
