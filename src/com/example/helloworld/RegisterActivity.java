
package com.example.helloworld;

import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends Activity {
	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellEmailAddress;
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
		fragInputCellEmailAddress= (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_email);
		fragInputCellPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);
	    
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		fragInputCellAccount.setLabelText("用户名");
		fragInputCellAccount.setHintText("请输入用户名:");
		
		fragInputCellEmailAddress.setLabelText("邮箱");
		fragInputCellEmailAddress.setHintText("请输入邮箱:");
		
		fragInputCellPassword.setLabelText("密码");
		fragInputCellPassword.setHintText("请输入密码:");
		fragInputCellPassword.setIsPassword(true);
		
		fragInputCellPasswordRepeat.setLabelText("再次输入密码");
		fragInputCellPasswordRepeat.setHintText("请再次输入密码:");
		fragInputCellPasswordRepeat.setIsPassword(true);
	}
	void goHelloWorld(){
		Intent itnt = new Intent(this,HelloWorldActivity.class);
		startActivity(itnt);
	}

}
