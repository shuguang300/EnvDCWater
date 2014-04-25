package com.env.dcwater.component;
import com.env.dcwater.util.NfcHelper;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 让activity继承该界面，可实现nfc的前台调度系统
 * 避免在程序中，使用其他nfc支持程序
 * @author sk
 *
 */
public class NfcActivity extends Activity{
	
	public static final String TAG_STRING = "NfcActivity";
	
	private NfcAdapter nfcAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if(nfcAdapter!=null){
			if(nfcAdapter.isEnabled()){
				nfcAdapter.enableForegroundDispatch(this, NfcHelper.NFCPendingIntent(this), NfcHelper.intentFilters, NfcHelper.techList);
			}
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(nfcAdapter!=null){
			nfcAdapter.disableForegroundDispatch(this);
		}		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);		
	}
}
