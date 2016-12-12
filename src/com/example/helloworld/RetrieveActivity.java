package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;
import com.example.helloworld.entity.User;
import com.example.helloworld.fragments.PasswordRecoverStep1Fragment;
import com.example.helloworld.fragments.PasswordRecoverStep1Fragment.OnGoNextListener;
import com.example.helloworld.fragments.PasswordRecoverStep2Fragment;
import com.example.helloworld.fragments.PasswordRecoverStep2Fragment.OnPwdRecoverListener;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RetrieveActivity extends Activity {
	PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
	PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_retrieve);

		step1.setOnGoNextListener(new OnGoNextListener() {

			@Override
			public void onGoNext() {
				goStep2();
			}
		});

		step2.setOnPwdRecoverListener(new OnPwdRecoverListener() {

			@Override
			public void onPwdRecover() {
				gopwdrecover();

			}
		});

		getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();

	}


	void goStep2(){
		getFragmentManager()
		.beginTransaction()		
		.setCustomAnimations(
				R.animator.slide_in_right,
				R.animator.slide_out_left1,
				R.animator.slide_in_left,
				R.animator.slide_out_right1)
		.replace(R.id.container, step2)
		.addToBackStack(null)
		.commit();
	}

	void gopwdrecover(){
		String email = step1.getText();
		String password =step2.getText();
		String passwordRepeat =step2.getText1();

		if(!password.equals(passwordRepeat)) {
			new AlertDialog.Builder(this)
			.setMessage("用户输入的密码不一致")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton("好",null)
			.show();

			return;
		}

//		password = MD5.getMD5(password);

		OkHttpClient client = Server.getSharedClient();

		MultipartBody requestBuilder = new MultipartBody.Builder()
				.addFormDataPart("email", email)
				.addFormDataPart("passwordHash", password)
				.build();

		Request request = Server.requestBuilderWithApi("passwordrecover")
				.method("post", null)
				.post(requestBuilder)
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				try{
					final String responseString = arg1.body().string();
//					ObjectMapper oMapper = new ObjectMapper();
//					final User user = oMapper.readValue(responseString, User.class); 

					runOnUiThread(new Runnable() {
						public void run() {
							RetrieveActivity.this.onResponse(arg0,responseString);
						}
					});
				}catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							RetrieveActivity.this.onFailure(arg0, e);
						}
					});
				}
			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						RetrieveActivity.this.onFailure(arg0, arg1);
					}
				});
			}
		});
	}
	void onResponse(Call arg0, String response) {
			new AlertDialog.Builder(this)
			.setTitle("修改成功")
			.setMessage(response)
			.setNegativeButton("OK", null)
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

