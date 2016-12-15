package com.example.helloworld;

import com.example.helloworld.api.Server;
import com.example.helloworld.fragments.widgets.AvatarView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NotesContentActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notecontent);
		
		String title=getIntent().getStringExtra("Title");
		String text=getIntent().getStringExtra("Text");
		String authorName=getIntent().getStringExtra("AuthorName");
		String date=getIntent().getStringExtra("Date");
	
		TextView textview1=(TextView)findViewById(R.id.textView1);
		TextView textview2=(TextView)findViewById(R.id.textView2);
		TextView textview3=(TextView)findViewById(R.id.textView3);
		TextView textview4=(TextView)findViewById(R.id.textView4);
		
		textview1.setText("原文标题:"+title);  
		textview2.setText("原文作者:"+authorName); 
		textview3.setText("评论内容:"+text); 
		textview4.setText("评论发表时间:"+date);
		
		AvatarView avatar = (AvatarView)findViewById(R.id.avatar);
		avatar.load(Server.ServerAdress+getIntent().getStringExtra("AuthorAvatar"));
	}
}
