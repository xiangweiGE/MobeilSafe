package com.example.mobeilsafe.activity;

import com.example.mobeilsafe.R;
import com.example.mobeilsafe.R.id;
import com.example.mobeilsafe.R.layout;
import com.example.mobeilsafe.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SplashActivity extends ActionBarActivity {

    private TextView tvVersion;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本号:"+getVersionName());
    }
    
    private String getVersionName(){
    	PackageManager packageManager = getPackageManager();
    	try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			//得到版本代码
			int versionCode = packageInfo.versionCode;
			//得到版本名字
			String versionName = packageInfo.versionName;
			System.out.println("版本代码为:"+versionCode+"版本名字为:"+versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			//无包名异常
			e.printStackTrace();
		}
    	return "";
    }
}
