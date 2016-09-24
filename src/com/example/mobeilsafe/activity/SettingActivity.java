package com.example.mobeilsafe.activity;

import com.example.mobeilsafe.R;
import com.example.mobeilsafe.view.SettingItemView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {
	private SettingItemView sivUpdate;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//新建SharePerferences存储勾选框结果
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("自动更新设置");
		boolean auto_update = mPref.getBoolean("auto_update", true);
		if(auto_update){
			//自动更新开启
			sivUpdate.setChecked(true);
		}
		else{
			//自动更新关闭
			sivUpdate.setChecked(false);
		}
		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断当前的勾选状态
				if (sivUpdate.isChecked()) {
					// 设置不勾选
					sivUpdate.setChecked(false);
					sivUpdate.setDesc("自动更新已关闭");
					// 更新sp
					mPref.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					sivUpdate.setDesc("自动更新已开启");
					// 更新sp
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
