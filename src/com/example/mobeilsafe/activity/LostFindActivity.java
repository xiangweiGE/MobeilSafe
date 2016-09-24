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
		//判断是否设置过向导
		boolean configed = mpref.getBoolean("config", false);
		
		if(configed){
			setContentView(R.layout.activity_lost_find);
		}else {
			// 跳转设置向导页
			startActivity(new Intent(this, Setup1Activity.class));
			finish();
		}
	}
	
	/**
	 * 重新进入设置向导
	 */
	
	public void reEnter(View view) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
}
