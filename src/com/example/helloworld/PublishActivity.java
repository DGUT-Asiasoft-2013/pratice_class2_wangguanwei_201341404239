package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.animation.LayoutTransition.TransitionListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PublishActivity extends Activity {
	EditText articleTitle;
	EditText articleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		articleTitle= (EditText)findViewById(R.id.art_title);
		articleText= (EditText)findViewById(R.id.art_text);
		
		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   sendContent();
				}
		});
	}

	void sendContent(){
		String title = articleTitle.getText().toString();
		String text = articleText.getText().toString();

		OkHttpClient client = Server.getSharedClient();

		MultipartBody requestBuilder = new MultipartBody.Builder()
				.addFormDataPart("title", title)
				.addFormDataPart("text", text)
				.build();

		Request request = Server.requestBuilderWithApi("article")
				.method("post", null)
				.post(requestBuilder)
				.build();
		
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				try{	     
					final String responString = arg1.body().toString();
					runOnUiThread(new Runnable() {
						public void run() {
							PublishActivity.this.onResponse(arg0,responString);
						}
					});
				}catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							PublishActivity.this.onFailure(arg0, e);
						}
					});
				}
			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
//						onFailure(arg0, arg1);
						PublishActivity.this.onFailure(arg0, arg1);
					}
				});

			}
		});
	}

	void onResponse(Call arg0, String responseBody) {
		new AlertDialog.Builder(this)
		.setTitle("发表成功")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Intent itnt =new Intent(PublishActivity.this,HelloWorldActivity.class);
				startActivity(itnt);
				overridePendingTransition(0,R.anim.slide_out_bottom);
				PublishActivity.this.finish();
			}
		})
		.show();
	}

	void onFailure(Call arg0, Exception arg1) {
		new AlertDialog.Builder(this)
		.setTitle("发表失败")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("OK", null)
		.show();
	}
}
