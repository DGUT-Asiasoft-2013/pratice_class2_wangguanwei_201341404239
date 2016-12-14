package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;
import com.example.helloworld.entity.Article;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddCommentActivity extends Activity{
	EditText commentText;
	Article article;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_comment);
		commentText=(EditText)findViewById(R.id.text);
		
		article= (Article)getIntent().getSerializableExtra("data");
		
		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendComment();
			}
		});
	}

	void sendComment(){
		String text = commentText.getText().toString();

		OkHttpClient client = Server.getSharedClient();

		MultipartBody requestBuilder = new MultipartBody.Builder()
				.addFormDataPart("text", text)
				.build();

		Request request = Server.requestBuilderWithApi("article/"+article.getId()+"/comments")
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
							AddCommentActivity.this.onResponse(arg0,responString);
						}
					});
				}catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							AddCommentActivity.this.onFailure(arg0, e);
						}
					});
				}
			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						AddCommentActivity.this.onFailure(arg0, arg1);
					}
				});

			}
		});
	}

	void onResponse(Call arg0, String responseBody) {
		new AlertDialog.Builder(this)
		.setTitle("����ɹ�")
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent itnt =new Intent(AddCommentActivity.this,HelloWorldActivity.class);
				startActivity(itnt);
				overridePendingTransition(0,R.anim.slide_out_bottom);
				AddCommentActivity.this.finish();
			}
		})
		.show();
	}

	void onFailure(Call arg0, Exception arg1) {
		new AlertDialog.Builder(this)
		.setTitle("����ʧ��")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("OK", null)
		.show();
	}

}
