package com.example.lauriswebapp1.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface MyResources extends ClientBundle {
	public static final MyResources INSTANCE = GWT.create(MyResources.class);

	public interface MyCss extends CssResource {
		String parent();
		String separate();
		String enlarge();
		String title();
		String addUp();
		String drop();
		String box();
	}
	
	@Source("my.css")
	public MyCss my();
}
