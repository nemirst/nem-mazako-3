package com.example.lauriswebapp1.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LaurisWebApp1Project implements EntryPoint {
	public void onModuleLoad() {
		MyResources.INSTANCE.my().ensureInjected();
		RootLayoutPanel.get().add(new MainMenu());
	}
}
