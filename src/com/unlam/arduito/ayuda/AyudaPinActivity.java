package com.unlam.arduito.ayuda;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.unlam.arduito.R;

public class AyudaPinActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ayuda_pin);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ayuda_pin, menu);
		return true;
	}

}
