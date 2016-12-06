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
	SimpleTextInputCellFragment fragPassword,fragPasswordRepeat,fragEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_retrieve);
//		fragEmail = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_email);
//		fragPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
//		fragPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);

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

//	@Override
//	protected void onResume() {
//		super.onResume();
//
//		fragEmail.setLabelText("” œ‰:");
//		fragEmail.setHintText("«Î ‰»Î” œ‰:");
//
//		fragEmail.setLabelText("√‹¬Î:");
//		fragEmail.setHintText("«Î ‰»Î√‹¬Î:");
//
//		fragEmail.setLabelText("” œ‰:");
//		fragEmail.setHintText("«Î ‰»Î‘Ÿ¥Œ ‰»Î√‹¬Î:");
//	}

//	void gosomewhere(){
//
//	}

	void goStep2(){

		getFragmentManager()
		.beginTransaction()		
		.replace(R.id.container, step2)
		.addToBackStack(null)
		.commit();
	}

}
