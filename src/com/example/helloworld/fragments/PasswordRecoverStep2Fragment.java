package com.example.helloworld.fragments;

import com.example.helloworld.R;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PasswordRecoverStep2Fragment extends Fragment {
	View view;
	SimpleTextInputCellFragment fragPassword,fragPasswordRepeat,fragverify;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view==null){
			view = inflater.inflate(R.layout.fragment_password_recover_step2, null);
		}
		fragverify = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_verify);
		fragPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
		fragPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);

		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();

		fragverify.setLabelText("验证码:");
		fragverify.setHintText("请输入验证码");

		fragPassword.setLabelText("密码:");
		fragPassword.setHintText("请输入密码");

		fragPasswordRepeat.setLabelText("再次输入密码:");
		fragPasswordRepeat.setHintText("请输入再次输入密码");
	}

}
