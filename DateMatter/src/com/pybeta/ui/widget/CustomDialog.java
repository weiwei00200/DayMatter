package com.pybeta.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class CustomDialog extends Dialog {
	private int mLayout;

	public CustomDialog(Context context, int layout,int style) {
		super(context, style);
		this.mLayout = layout;

	}
	public CustomDialog(Context context){
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// set content
		setContentView(mLayout);
	}
}
