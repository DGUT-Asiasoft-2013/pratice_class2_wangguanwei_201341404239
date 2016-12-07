package com.example.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FeedContentActivity extends Activity {
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feedcontent);
		
		String text=getIntent().getStringExtra("Text");
		
		textView=(TextView)findViewById(R.id.textView1);
	    textView.setText(text);  
	}
}
