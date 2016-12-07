package com.example.helloworld;

import com.example.helloworld.fragments.PasswordRecoverStep1Fragment;
import com.example.helloworld.fragments.PasswordRecoverStep1Fragment.OnGoNextListener;
import com.example.helloworld.fragments.PasswordRecoverStep2Fragment;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RetrieveActivity extends Activity {
	PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
	PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_retrieve);

//		findViewById(R.id.btn_retrieve).setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				gosomewhere();
//
//			}
//		});

		step1.setOnGoNextListener(new OnGoNextListener() {

			@Override
			public void onGoNext() {
				goStep2();
			}
		});

		getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();

	}


//	void gosomewhere(){
//
//	}

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

}
