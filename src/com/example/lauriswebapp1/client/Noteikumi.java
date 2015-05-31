package com.example.lauriswebapp1.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class Noteikumi extends Composite {
	private static NoteikumiUiBinder uiBinder = GWT
			.create(NoteikumiUiBinder.class);
	interface NoteikumiUiBinder extends UiBinder<Widget, Noteikumi> {
	}
	@UiField DialogBox dialogBox;
	public Noteikumi() {
		initWidget(uiBinder.createAndBindUi(this));
		dialogBox.center();
	}

	public void show() {
		dialogBox.show();
	}
}
