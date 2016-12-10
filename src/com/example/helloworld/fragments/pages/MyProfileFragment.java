package com.example.helloworld.fragments.pages;

import java.io.IOException;

import com.example.helloworld.R;
import com.example.helloworld.RegisterActivity;
import com.example.helloworld.api.Server;
import com.example.helloworld.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyProfileFragment extends Fragment {

	TextView textview;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view==null){
			view= inflater.inflate(R.layout.fragment_page_my_profile, null);
			textview = (TextView)view.findViewById(R.id.textview1);
		}
		
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		
		OkHttpClient client = Server.getSharedClient();
		
		Request request =Server.requestBuilderWithApi("me")
				.method("get", null)
				.build();
		
		client.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {

				ObjectMapper objectMapper = new ObjectMapper();
				final User user = objectMapper.readValue(arg1.body().string(), User.class);

				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						MyProfileFragment.this.onResponse(arg0, user.getAccount());
						
					}
				});
				
			}
			
			@Override
			public void onFailure(final Call arg0, final IOException arg1) {

				new Runnable() {
					@Override
					public void run() {
						MyProfileFragment.this.onFailure(arg0, arg1);
					}
				};
				
			}
		});
	}
	
	void onResponse(Call arg0, String responseBody) {
		textview.setText(responseBody);
   
	}
	
	void onFailure(Call arg0, Exception arg1) {
		Toast.makeText(getActivity(), "´íÎó£¡", Toast.LENGTH_SHORT);
	}
}
