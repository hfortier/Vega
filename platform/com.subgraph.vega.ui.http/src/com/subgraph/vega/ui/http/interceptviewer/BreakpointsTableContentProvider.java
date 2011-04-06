package com.subgraph.vega.ui.http.interceptviewer;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.subgraph.vega.api.http.proxy.IHttpInterceptor;
import com.subgraph.vega.api.http.proxy.IProxyTransaction.TransactionDirection;
import com.subgraph.vega.api.model.conditions.IHttpCondition;

public class BreakpointsTableContentProvider implements IStructuredContentProvider {
	private final TransactionDirection direction;
	private IHttpInterceptor interceptor;

	public BreakpointsTableContentProvider(TransactionDirection direction) {
		this.direction = direction;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		interceptor = (IHttpInterceptor) newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return interceptor.getBreakpointSet(direction).getAllConditions().toArray(new IHttpCondition[0]);
	}

}
