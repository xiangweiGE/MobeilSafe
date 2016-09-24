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
		//�½�SharePerferences�洢��ѡ����
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("�Զ���������");
		boolean auto_update = mPref.getBoolean("auto_update", true);
		if(auto_update){
			//�Զ����¿���
			sivUpdate.setChecked(true);
		}
		else{
			//�Զ����¹ر�
			sivUpdate.setChecked(false);
		}
		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �жϵ�ǰ�Ĺ�ѡ״̬
				if (sivUpdate.isChecked()) {
					// ���ò���ѡ
					sivUpdate.setChecked(false);
					sivUpdate.setDesc("�Զ������ѹر�");
					// ����sp
					mPref.edit().putBoolean("auto_update", false).commit();
				} else {
					sivUpdate.setChecked(true);
					sivUpdate.setDesc("�Զ������ѿ���");
					// ����sp
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
