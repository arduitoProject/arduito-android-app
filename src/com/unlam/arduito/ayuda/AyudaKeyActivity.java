package com.unlam.arduito.ayuda;

import com.unlam.arduito.R;
import com.unlam.arduito.R.layout;
import com.unlam.arduito.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AyudaKeyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ayuda_key);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ayuda_key, menu);
		return true;
	}

}
