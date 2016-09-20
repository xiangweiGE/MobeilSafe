package com.example.mobeilsafe.activity;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobeilsafe.R;
import com.example.mobeilsafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends ActionBarActivity {

	private TextView tvVersion;
	private TextView tvProgress;
	private static final int CODE_UPDATE_DIALOG = 0;
	private static final int CODE_URL_ERROR = 1;
	private static final int CODE_IOE_ERROR = 2;
	private static final int CODE_JSON_ERROR = 3;
	private static final int CODE_ENTER_HOME = 4;

	private String mversionName;
	private int mversionCode;
	private String mdecription;
	private String mdownloadUrl;

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				// 弹出升级对话框
				showUpdateDialog();
				break;
			case CODE_URL_ERROR:
				errorHome();
				Toast.makeText(SplashActivity.this, "URL解析错误",
						Toast.LENGTH_SHORT).show();
				break;
			case CODE_IOE_ERROR:
				errorHome();
				Toast.makeText(SplashActivity.this, "网络连接错误",
						Toast.LENGTH_SHORT).show();
				break;
			case CODE_JSON_ERROR:
				errorHome();
				Toast.makeText(SplashActivity.this, "JSON解析错误",
						Toast.LENGTH_SHORT).show();
				break;
			case CODE_ENTER_HOME:
				errorHome();
				break;
			default:
				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("版本号:" + getVersionName());
		tvProgress = (TextView) findViewById(R.id.tv_progress);
		// 执行检查版本号的方法
		checkVersion();
	}

	/**
	 * 显示升级对话框的方法
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("最新版本" + mversionName);
		builder.setMessage(mdecription);
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("立即更新");
				download();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				errorHome();
			}
		});
		builder.show();
	}

	/**
	 * 下载安装文件
	 */
	protected void download() {
		// 使用Xutis框架下载更新,事先检查是否挂载SD卡
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			tvProgress.setVisibility(View.VISIBLE);// 显示进度
			// 设置存放路径
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			// xUtils
			HttpUtils utils = new HttpUtils();
			utils.download(mdownloadUrl, target, new RequestCallBack<File>() {
				private ResponseInfo<File> arg0;

				// 下载文件进度
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println("下载进度:" + current + "/" + total);
					tvProgress.setText("下载进度:" + current * (100 / total));
				}

				// 下载成功
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					System.out.println("下载成功");
					// 跳转到系统下载页面
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					/*intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");*/
					// startActivity(intent);
					startActivityForResult(intent, 0);// 如果用户取消安装的话,
														// 会返回结果,回调方法onActivityResult
				}

				// 下载失败
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "下载失败",
							Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "没有找到sdcard!",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 返回主页面
	 */
	protected void errorHome() {
		// 返回主页面
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 检查版本更新
	 */
	private void checkVersion() {
		final long startTime = System.currentTimeMillis();
		// 网络连接不能在主线程中执行
		new Thread() {

			public void run() {
				Message msg = Message.obtain();
				HttpURLConnection connection = null;
				try {
					URL url = new URL("http://10.0.2.2:8080/update.json");
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);
					connection.connect();// 连接服务器

					int responseCode = connection.getResponseCode();// 获取响应码
					// 判断响应码
					if (responseCode == 200) {
						InputStream inputStream = connection.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						System.out.println(result);// 打印输出流
						// 使用JSON解析
						JSONObject jo = new JSONObject(result);
						mversionName = jo.getString("versionName");
						mversionCode = jo.getInt("versionCode");
						mdecription = jo.getString("decription");
						mdownloadUrl = jo.getString("downloadUrl");

						if (mversionCode > getVersionCode()) {
							// 表示版本有更新
							msg.what = CODE_UPDATE_DIALOG;
							System.out.println("++++++++++++++++版本号有升级");
						}
					}
				} catch (MalformedURLException e) {
					// URL路径异常时调用
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// 网络连接异常调用
					msg.what = CODE_IOE_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// JSON解析异常时调用
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;
					if (2000 > timeUsed) {
						try {
							// 强制休眠到达2秒
							sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				mhandler.sendMessage(msg);
				// 如果连接不为null
				if (connection != null) {
					connection.disconnect();
				}
			}
		}.start();

	}

	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			// 得到版本代码
			int versionCode = packageInfo.versionCode;
			// 得到版本名字
			String versionName = packageInfo.versionName;
			System.out.println("版本代码为:" + versionCode + "版本名字为:" + versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			// 无包名异常
			e.printStackTrace();
		}
		return "";
	}

	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			// 得到版本代码
			int versionCode = packageInfo.versionCode;
			// 得到版本名字
			String versionName = packageInfo.versionName;
			System.out.println("版本代码为:" + versionCode + "版本名字为:" + versionName);
			return versionCode;
		} catch (NameNotFoundException e) {
			// 无包名异常
			e.printStackTrace();
		}
		return -1;
	}
}
