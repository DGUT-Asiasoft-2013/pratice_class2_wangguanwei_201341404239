package com.example.helloworld;

import java.security.PublicKey;

import com.example.helloworld.fragments.pages.FeedListFragment;
import com.example.helloworld.fragments.pages.MyProfileFragment;
import com.example.helloworld.fragments.pages.NotesListFragment;
import com.example.helloworld.fragments.pages.SearchFragment;
import com.example.helloworld.fragments.widgets.MainTabbarFragment;
import com.example.helloworld.fragments.widgets.MainTabbarFragment.OnTabSelectedListener;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class HelloWorldActivity extends Activity {
	FeedListFragment contentFeedList = new FeedListFragment();
	NotesListFragment contentNoteList = new NotesListFragment();
	SearchFragment contentSearchpage= new SearchFragment();
	MyProfileFragment contentMyProfile = new MyProfileFragment();
	MainTabbarFragment tabbar;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helloworld);
		
		tabbar =(MainTabbarFragment)getFragmentManager().findFragmentById(R.id.frag_tabbar);
	    tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {
			
			@Override
			public void OnTabSelected(int index) {
				changeContentFragment(index);
				
			}
		}); 
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		tabbar.setSelectedItem(0);
	}
	
	void changeContentFragment(int index){
		Fragment newFrag = null;
		
		switch (index) {
		case 0:newFrag = contentFeedList;break;
		case 1:newFrag = contentNoteList;break;
		case 2:newFrag = contentSearchpage;break;
		case 3:newFrag = contentMyProfile;break;
		
		default:
			break;
		}
		if(newFrag==null) return;
		
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content, newFrag)
		.commit();
	}
}
