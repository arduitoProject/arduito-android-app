package com.unlam.arduito.ayuda;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.unlam.arduito.R;

public class AyudaArduitoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ayuda_arduito);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ayuda_arduito, menu);
		return true;
	}

}
