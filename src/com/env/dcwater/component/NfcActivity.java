package com.env.dcwater.component;
import com.env.dcwater.util.NfcUtil;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.KeyEvent;

public class NfcActivity extends Activity{
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
				nfcAdapter.enableForegroundDispatch(this, NfcUtil.NFCPendingIntent(this), NfcUtil.intentFilters, NfcUtil.techList);
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
