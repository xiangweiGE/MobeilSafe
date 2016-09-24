package com.example.mobeilsafe.activity;

import com.example.mobeilsafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class LostFindActivity extends Activity{
	private SharedPreferences mpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mpref = getSharedPreferences("config", MODE_PRIVATE);
		//�ж��Ƿ����ù���
		boolean configed = mpref.getBoolean("config", false);
		
		if(configed){
			setContentView(R.layout.activity_lost_find);
		}else {
			// ��ת������ҳ
			startActivity(new Intent(this, Setup1Activity.class));
			finish();
		}
	}
	
	/**
	 * ���½���������
	 */
	
	public void reEnter(View view) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
}
