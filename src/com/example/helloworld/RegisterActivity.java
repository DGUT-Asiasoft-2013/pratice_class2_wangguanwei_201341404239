package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;
import com.example.helloworld.fragments.inputcells.PictureInputCellFragment;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {

	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment	fragInputCellEmailAdress;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	SimpleTextInputCellFragment fragInputCellName;
	PictureInputCellFragment fragInputCellAvatar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		fragInputCellAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		fragInputCellAvatar = (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_avatar);
		fragInputCellEmailAdress = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);
		fragInputCellName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);
	
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submit();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fragInputCellAccount.setLabelText("用户名");
		fragInputCellAccount.setHintText("请输入用户名");
		fragInputCellEmailAdress.setLabelText("电子邮箱");
		fragInputCellEmailAdress.setHintText("请输入电子邮箱");
		fragInputCellPassword.setLabelText("密码");
		fragInputCellPassword.setHintText("请输入密码");
		fragInputCellPassword.setIsPassword(true);
		fragInputCellPasswordRepeat.setLabelText("重复密码");
		fragInputCellPasswordRepeat.setHintText("请输入重复密码");
		fragInputCellPasswordRepeat.setIsPassword(true);
		fragInputCellName.setLabelText("昵称");
		fragInputCellName.setHintText("请输入昵称");
		fragInputCellAvatar.setLabelText("选择图片");
		fragInputCellAvatar.setHintText("请输入图片");
	}
	
	void submit() {
		String password = fragInputCellPassword.getText();
		String passwordRepeat = fragInputCellPasswordRepeat.getText();
		if(!password.equals(passwordRepeat)) {
			new AlertDialog.Builder(this)
			.setMessage("用户输入的密码不一致")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton("好",null)
			.show();
			
			return;
		}
		
		password = MD5.getMD5(password);
		String account = fragInputCellAccount.getText();
		String email = fragInputCellEmailAdress.getText();
		String name = fragInputCellName.getText();
	
		
		OkHttpClient client = Server.getSharedClient();
		
		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				.addFormDataPart("account", account)
				.addFormDataPart("name", name)
				.addFormDataPart("email", email)
				.addFormDataPart("passwordHash", password);
				
		
		if(fragInputCellAvatar.getPngData() != null) {
			requestBodyBuilder.addFormDataPart("avatar", "avatar", RequestBody
					.create(MediaType.parse("image/png"), fragInputCellAvatar.getPngData()));
		}
		
		Request request = Server.requestBuilderWithApi("register")
				.method("post", null).post(requestBodyBuilder.build())
				.build();
		
		final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
		progressDialog.setMessage("请稍后");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				try{
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							try {
								RegisterActivity.this.onResponse(arg0,  arg1.body().string());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								RegisterActivity.this.onFailure(arg0, e);
							}
						}
					});
				}catch (final Exception e) {
					RegisterActivity.this.onFailure(arg0, e);
				}
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();
						RegisterActivity.this.onFailure(arg0, arg1);
					}
				});
			}
		});
	}
	
	void onResponse(Call arg0, String responseBody) {
		new AlertDialog.Builder(this)
		.setTitle("注册成功")
		.setMessage(responseBody)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		})
		.show();
	}
	
	void onFailure(Call arg0, Exception arg1) {
		new AlertDialog.Builder(this)
		.setTitle("注册失败")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("OK", null)
		.show();
	}
}
