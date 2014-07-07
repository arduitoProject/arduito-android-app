package com.unlam.arduito.ayuda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.unlam.arduito.R;

public class AyudaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ayuda);
		
		/************	inicio botones *****************************/

		Button	botonPin = (Button) findViewById(R.id.btnAyudaPin);
		botonPin.setOnClickListener(new OnClickListener() {
		       public void onClick(View v) {
		          Intent intent = new Intent(AyudaActivity.this, AyudaPinActivity.class);
		          startActivity(intent);
		       }
		});
		
		Button	botonArduito = (Button) findViewById(R.id.btnAyudaArduito);
		botonArduito.setOnClickListener(new OnClickListener() {
		       public void onClick(View v) {
		          Intent intent = new Intent(AyudaActivity.this, AyudaArduitoActivity.class);
		          startActivity(intent);
		       }
		});
		
		Button	botonKey = (Button) findViewById(R.id.btnAyudaGCM);
		botonKey.setOnClickListener(new OnClickListener() {
		       public void onClick(View v) {
		          Intent intent = new Intent(AyudaActivity.this, AyudaKeyActivity.class);
		          startActivity(intent);
		       }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ayuda, menu);
		return true;
	}

}
