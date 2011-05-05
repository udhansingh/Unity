package org.onesun.sfs.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FeedVu implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private RootPanel mainPanel = RootPanel.get("root-container");
	private AggregatedView mainView = new AggregatedView();
	private Connector connector = new Connector(mainView);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Setup the main screen view
		mainPanel.add(mainView);
		
		connector.connect();
	}
}
