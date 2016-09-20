package com.example.mobeilsafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView {
	//有style样式会走此方法
	public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	//有属性时走此方法
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	//用代码new时，走此方法
	public FocusedTextView(Context context) {
		super(context);
	}
	/**
	 * 表示有没有焦点，跑马灯要运行，要先确定焦点，如果为true，则强制运行
	 */
	public boolean isFocused(){
		return true;
	}
}
