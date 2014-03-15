package com.pybeta.ui.widget;

import com.pybeta.daymatter.R;
import com.pybeta.widget.NumberPicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class NumberPickerDialog extends Dialog implements OnClickListener, NumberPicker.OnValueChangeListener,android.view.View.OnClickListener {

	private static final String NUMBER = "NUMBER";
	
    private final NumberPicker mNumberPicker;
    private final OnNumberSetListener mCallBack;
    
    
    
    public interface OnNumberSetListener {
        void onDateSet(NumberPicker view, int value);
    }
    
    public NumberPickerDialog(Context context, OnNumberSetListener callBack, int value) {
//		this(context, Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ? R.style.Theme_Dialog_Alert : 0, callBack, value);
    	this(context, R.style.SyncDialog, callBack, value);
	}
	
    public NumberPickerDialog(Context context, int theme, OnNumberSetListener callBack, int value) {
    	super(context, theme);
    	
    	mCallBack = callBack;
    	
        Context themeContext = getContext();
//        setButton(BUTTON_POSITIVE, themeContext.getText(R.string.date_time_set), this);
//        setButton(BUTTON_NEGATIVE, themeContext.getText(R.string.cancel), (OnClickListener) null);
//        setIcon(0);
//        setTitle(R.string.number_picker_dialog_title);

        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.number_picker_dialog, null);
//        setView(view);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(10000);
        mNumberPicker.setValue(value);
        mNumberPicker.setOnValueChangedListener(this);
        
        
        this.setContentView(view);
    }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (mCallBack != null) {
			mNumberPicker.clearFocus();
			mCallBack.onDateSet(mNumberPicker, mNumberPicker.getValue());
		}
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		mNumberPicker.setValue(newVal);
	}
	
    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(NUMBER, mNumberPicker.getValue());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int value = savedInstanceState.getInt(NUMBER);
        mNumberPicker.setValue(value);
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
