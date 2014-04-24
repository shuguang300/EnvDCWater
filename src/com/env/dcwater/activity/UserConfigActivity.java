package com.env.dcwater.activity;

import android.os.Bundle;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;

public class UserConfigActivity extends NfcActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userconfig);
	}
}
