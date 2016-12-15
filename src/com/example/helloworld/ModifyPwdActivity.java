package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModifyPwdActivity extends Activity {
	Button btn_modify;
	TextView textPwd,textPwdRepeat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify);
		
		textPwd=(TextView)findViewById(R.id.pwd);
		textPwdRepeat=(TextView)findViewById(R.id.pwd_repeat);
		btn_modify=(Button)findViewById(R.id.btn_change);
		
		btn_modify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				modifypwd();
			}
		});
	}
	void modifypwd(){
		String password =textPwd.getText().toString();
		String passwordRepeat =textPwdRepeat.getText().toString();

		if(!password.equals(passwordRepeat)) {
			new AlertDialog.Builder(this)
			.setMessage("用户输入的密码不一致")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton("好",null)
			.show();

			return;
		}

		//	password = MD5.getMD5(password);

		OkHttpClient client = Server.getSharedClient();

		MultipartBody requestBuilder = new MultipartBody.Builder()
				.addFormDataPart("passwordHash", password)
				.build();

		Request request = Server.requestBuilderWithApi("modifypassword")
				.method("post", null)
				.post(requestBuilder)
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				try{
					final String responseString = arg1.body().string();
					runOnUiThread(new Runnable() {
						public void run() {
							ModifyPwdActivity.this.onResponse(arg0,responseString);
						}
					});
				}catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							ModifyPwdActivity.this.onFailure(arg0, e);
						}
					});
				}
			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						ModifyPwdActivity.this.onFailure(arg0, arg1);
					}
				});
			}
		});
	}
	void onResponse(Call arg0, String response) {
		new AlertDialog.Builder(this)
		.setMessage("修改成功")
		.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent itnt = new Intent(ModifyPwdActivity.this,HelloWorldActivity.class);
				startActivity(itnt);
			}
		})
		.show();
	}

	void onFailure(Call arg0, Exception arg1) {
		new AlertDialog.Builder(this)
		.setTitle("修改失败")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("OK", null)
		.show();
	}
}
