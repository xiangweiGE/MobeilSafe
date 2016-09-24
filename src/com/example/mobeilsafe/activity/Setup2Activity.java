package com.example.mobeilsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobeilsafe.R;

public class Setup2Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
	}
	
	public void next(View v){
		startActivity(new Intent(this,Setup3Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	
	public void previous(View v) {
		// TODO Auto-generated method stub
		startActivity(new Intent(this,Setup1Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// 进入动画和退出动画
	}
}
