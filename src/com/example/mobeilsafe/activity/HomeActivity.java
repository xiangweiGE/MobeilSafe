package com.example.mobeilsafe.activity;


import com.example.mobeilsafe.R;
import com.example.mobeilsafe.utils.MD5Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private GridView gvHome;
	private String[] mItems = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
			"缓存清理", "高级工具", "设置中心" };
	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };
	private SharedPreferences mPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		/**
		 * 为gvHome设置点击侦听
		 * @author Administrator
		 *
		 */
		
		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				
				case 0:
					//跳转至手机防盗
					showPasswordDialog();
					break;
				case 8:
					//跳转至SettingActivity
					startActivity(new Intent(HomeActivity.this,SettingActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}
	
	/**
	 * 显示密码弹窗
	 * @return 
	 */
	
	public void showPasswordDialog(){
		//判断是否已设置密码
		String savePassword = mPref.getString("password", null);
		if (!TextUtils.isEmpty(savePassword)) {
			// 输入密码弹窗
			showPasswordInputDialog();
		} else {
			// 如果没有设置过, 弹出设置密码的弹窗
			showPasswordSetDialog();
		}
	}
	
	/**
	 * 输入密码弹窗
	 */
	private void showPasswordInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(this, R.layout.dialog_input_password, null);
		// dialog.setView(view);// 将自定义的布局文件设置给dialog
		dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);

		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();

				if (!TextUtils.isEmpty(password)) {
					String savedPassword = mPref.getString("password", null);

					if (MD5Utils.encode(password).equals(savedPassword)) {
						// Toast.makeText(HomeActivity.this, "登录成功!",
						// Toast.LENGTH_SHORT).show();
						dialog.dismiss();

						// 跳转到手机防盗页
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "密码错误!",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框内容不能为空!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();// 隐藏dialog
			}
		});

		dialog.show();
	}
	
	
	private void showPasswordSetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		//将设置密码框布局塞入自定义布局
		View view = View.inflate(this, R.layout.dailog_set_password, null);
		//将自定义布局塞入对话框
		dialog.setView(view);
		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view
				.findViewById(R.id.et_password_confirm);
		
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		//设置确定按钮单击时间
		btnOK.setOnClickListener(new OnClickListener() {
			String password = etPassword.getText().toString();
			String passwordconfirm = etPasswordConfirm.getText().toString();
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(password.equals(passwordconfirm) && !password.isEmpty()){
					//将密码保存
					mPref.edit().putString("password", MD5Utils.encode(password)).commit();
					dialog.dismiss();
					// 跳转到手机防盗页
					startActivity(new Intent(HomeActivity.this,
							LostFindActivity.class));
				}else{
					Toast.makeText(HomeActivity.this, "两次密码不一致或空密码!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}


	class HomeAdapter extends BaseAdapter {
		// 设置数量
		@Override
		public int getCount() {
			return mItems.length;
		}

		// 设置选中条目
		@Override
		public Object getItem(int position) {
			return mItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,
					R.layout.home_list_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
			tvItem.setText(mItems[position]);
			ivItem.setImageResource(mPics[position]);
			return view;
		}

	}
}
