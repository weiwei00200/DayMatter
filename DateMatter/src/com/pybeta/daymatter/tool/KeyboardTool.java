package com.pybeta.daymatter.tool;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class KeyboardTool {
	
	public static void isShowKeyBoard(boolean isShow, EditText editText,Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (isShow) {
			imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
		} else {

			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}

	}

}
