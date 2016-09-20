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
				// ���������Ի���
				showUpdateDialog();
				break;
			case CODE_URL_ERROR:
				errorHome();
				Toast.makeText(SplashActivity.this, "URL��������",
						Toast.LENGTH_SHORT).show();
				break;
			case CODE_IOE_ERROR:
				errorHome();
				Toast.makeText(SplashActivity.this, "�������Ӵ���",
						Toast.LENGTH_SHORT).show();
				break;
			case CODE_JSON_ERROR:
				errorHome();
				Toast.makeText(SplashActivity.this, "JSON��������",
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
		tvVersion.setText("�汾��:" + getVersionName());
		tvProgress = (TextView) findViewById(R.id.tv_progress);
		// ִ�м��汾�ŵķ���
		checkVersion();
	}

	/**
	 * ��ʾ�����Ի���ķ���
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("���°汾" + mversionName);
		builder.setMessage(mdecription);
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("��������");
				download();
			}
		});
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				errorHome();
			}
		});
		builder.show();
	}

	/**
	 * ���ذ�װ�ļ�
	 */
	protected void download() {
		// ʹ��Xutis������ظ���,���ȼ���Ƿ����SD��
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			tvProgress.setVisibility(View.VISIBLE);// ��ʾ����
			// ���ô��·��
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			// xUtils
			HttpUtils utils = new HttpUtils();
			utils.download(mdownloadUrl, target, new RequestCallBack<File>() {
				private ResponseInfo<File> arg0;

				// �����ļ�����
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println("���ؽ���:" + current + "/" + total);
					tvProgress.setText("���ؽ���:" + current * (100 / total));
				}

				// ���سɹ�
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					System.out.println("���سɹ�");
					// ��ת��ϵͳ����ҳ��
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					/*intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");*/
					// startActivity(intent);
					startActivityForResult(intent, 0);// ����û�ȡ����װ�Ļ�,
														// �᷵�ؽ��,�ص�����onActivityResult
				}

				// ����ʧ��
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "����ʧ��",
							Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "û���ҵ�sdcard!",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * ������ҳ��
	 */
	protected void errorHome() {
		// ������ҳ��
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * ���汾����
	 */
	private void checkVersion() {
		final long startTime = System.currentTimeMillis();
		// �������Ӳ��������߳���ִ��
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
					connection.connect();// ���ӷ�����

					int responseCode = connection.getResponseCode();// ��ȡ��Ӧ��
					// �ж���Ӧ��
					if (responseCode == 200) {
						InputStream inputStream = connection.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						System.out.println(result);// ��ӡ�����
						// ʹ��JSON����
						JSONObject jo = new JSONObject(result);
						mversionName = jo.getString("versionName");
						mversionCode = jo.getInt("versionCode");
						mdecription = jo.getString("decription");
						mdownloadUrl = jo.getString("downloadUrl");

						if (mversionCode > getVersionCode()) {
							// ��ʾ�汾�и���
							msg.what = CODE_UPDATE_DIALOG;
							System.out.println("++++++++++++++++�汾��������");
						}
					}
				} catch (MalformedURLException e) {
					// URL·���쳣ʱ����
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// ���������쳣����
					msg.what = CODE_IOE_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// JSON�����쳣ʱ����
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;
					if (2000 > timeUsed) {
						try {
							// ǿ�����ߵ���2��
							sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				mhandler.sendMessage(msg);
				// ������Ӳ�Ϊnull
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
			// �õ��汾����
			int versionCode = packageInfo.versionCode;
			// �õ��汾����
			String versionName = packageInfo.versionName;
			System.out.println("�汾����Ϊ:" + versionCode + "�汾����Ϊ:" + versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			// �ް����쳣
			e.printStackTrace();
		}
		return "";
	}

	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			// �õ��汾����
			int versionCode = packageInfo.versionCode;
			// �õ��汾����
			String versionName = packageInfo.versionName;
			System.out.println("�汾����Ϊ:" + versionCode + "�汾����Ϊ:" + versionName);
			return versionCode;
		} catch (NameNotFoundException e) {
			// �ް����쳣
			e.printStackTrace();
		}
		return -1;
	}
}
