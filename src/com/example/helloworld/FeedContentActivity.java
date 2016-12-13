package com.example.helloworld;

import com.example.helloworld.api.Server;
import com.example.helloworld.entity.Article;
import com.example.helloworld.fragments.widgets.AvatarView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FeedContentActivity extends Activity {
	@SuppressWarnings("null")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feedcontent);
		
		String title=getIntent().getStringExtra("Title");
		String text=getIntent().getStringExtra("Text");
		String authorName=getIntent().getStringExtra("AuthorName");
		String date=getIntent().getStringExtra("Date");
		

		TextView textview1=(TextView)findViewById(R.id.textView1);
		TextView textview2=(TextView)findViewById(R.id.textView2);
		TextView textview3=(TextView)findViewById(R.id.textView3);
		TextView textview4=(TextView)findViewById(R.id.textView4);
		AvatarView avatar = (AvatarView)findViewById(R.id.avatar);
		avatar.load(Server.ServerAdress+getIntent().getStringExtra("AuthorAvatar"));
		

	    textview1.setText("标题:"+title);  
	    textview2.setText("作者:"+authorName); 
	    textview3.setText("文章内容:"+text); 
	    textview4.setText("发表时间:"+date); 
	    
 
	    findViewById(R.id.btn_comment).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	    
	}
}
