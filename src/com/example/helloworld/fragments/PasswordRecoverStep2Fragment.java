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

		fragverify.setLabelText("��֤��:");
		fragverify.setHintText("��������֤��");

		fragPassword.setLabelText("����:");
		fragPassword.setHintText("����������");

		fragPasswordRepeat.setLabelText("�ٴ���������:");
		fragPasswordRepeat.setHintText("�������ٴ���������");
	}

}
