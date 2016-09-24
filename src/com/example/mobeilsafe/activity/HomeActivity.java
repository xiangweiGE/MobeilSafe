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
	private String[] mItems = { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���", "����ͳ��", "�ֻ�ɱ��",
			"��������", "�߼�����", "��������" };
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
		 * ΪgvHome���õ������
		 * @author Administrator
		 *
		 */
		
		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				
				case 0:
					//��ת���ֻ�����
					showPasswordDialog();
					break;
				case 8:
					//��ת��SettingActivity
					startActivity(new Intent(HomeActivity.this,SettingActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}
	
	/**
	 * ��ʾ���뵯��
	 * @return 
	 */
	
	public void showPasswordDialog(){
		//�ж��Ƿ�����������
		String savePassword = mPref.getString("password", null);
		if (!TextUtils.isEmpty(savePassword)) {
			// �������뵯��
			showPasswordInputDialog();
		} else {
			// ���û�����ù�, ������������ĵ���
			showPasswordSetDialog();
		}
	}
	
	/**
	 * �������뵯��
	 */
	private void showPasswordInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(this, R.layout.dialog_input_password, null);
		// dialog.setView(view);// ���Զ���Ĳ����ļ����ø�dialog
		dialog.setView(view, 0, 0, 0, 0);// ���ñ߾�Ϊ0,��֤��2.x�İ汾������û����

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
						// Toast.makeText(HomeActivity.this, "��¼�ɹ�!",
						// Toast.LENGTH_SHORT).show();
						dialog.dismiss();

						// ��ת���ֻ�����ҳ
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "�������!",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "��������ݲ���Ϊ��!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();// ����dialog
			}
		});

		dialog.show();
	}
	
	
	private void showPasswordSetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		//����������򲼾������Զ��岼��
		View view = View.inflate(this, R.layout.dailog_set_password, null);
		//���Զ��岼������Ի���
		dialog.setView(view);
		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view
				.findViewById(R.id.et_password_confirm);
		
		Button btnOK = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		//����ȷ����ť����ʱ��
		btnOK.setOnClickListener(new OnClickListener() {
			String password = etPassword.getText().toString();
			String passwordconfirm = etPasswordConfirm.getText().toString();
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(password.equals(passwordconfirm) && !password.isEmpty()){
					//�����뱣��
					mPref.edit().putString("password", MD5Utils.encode(password)).commit();
					dialog.dismiss();
					// ��ת���ֻ�����ҳ
					startActivity(new Intent(HomeActivity.this,
							LostFindActivity.class));
				}else{
					Toast.makeText(HomeActivity.this, "�������벻һ�»������!",
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
		// ��������
		@Override
		public int getCount() {
			return mItems.length;
		}

		// ����ѡ����Ŀ
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
