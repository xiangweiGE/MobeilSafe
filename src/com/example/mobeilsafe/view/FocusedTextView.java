package com.example.mobeilsafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView {
	//��style��ʽ���ߴ˷���
	public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	//������ʱ�ߴ˷���
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	//�ô���newʱ���ߴ˷���
	public FocusedTextView(Context context) {
		super(context);
	}
	/**
	 * ��ʾ��û�н��㣬�����Ҫ���У�Ҫ��ȷ�����㣬���Ϊtrue����ǿ������
	 */
	public boolean isFocused(){
		return true;
	}
}
