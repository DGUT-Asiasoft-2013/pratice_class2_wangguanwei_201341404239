
package com.example.helloworld;

import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends Activity {
	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		
		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goHelloWorld();
				
			}
		});
		
		
		fragInputCellAccount = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_account);
		fragInputCellPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);
	    
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fragInputCellAccount.setLabel("�û���");
		fragInputCellAccount.setHintText("�������û���:");
		fragInputCellPassword.setLabel("����");
		fragInputCellPassword.setHintText("����������:");
		fragInputCellPassword.setIsPassword(true);
		fragInputCellPasswordRepeat.setLabel("�ٴ���������");
		fragInputCellPasswordRepeat.setHintText("���ٴ���������:");
		fragInputCellPasswordRepeat.setIsPassword(true);
	}
	void goHelloWorld(){
		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);
	}

}
