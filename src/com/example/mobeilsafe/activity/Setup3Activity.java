package com.example.mobeilsafe.activity;

import com.example.mobeilsafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup3Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	
	public void next(View v){
		startActivity(new Intent(this,Setup4Activity.class));
		finish();
		//两个界面切换动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	
	public void previous(View v) {
		startActivity(new Intent(this,Setup2Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in,R.anim.tran_previous_out);
	}
}
