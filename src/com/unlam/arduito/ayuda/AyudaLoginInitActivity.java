package com.unlam.arduito.ayuda;

import com.unlam.arduito.MainActivity;
import com.unlam.arduito.R;
import com.unlam.arduito.R.layout;
import com.unlam.arduito.R.menu;
import com.unlam.arduito.configuracion.ConfiguracionGeneralActivity;
import com.unlam.arduito.login.LoginSetDomainActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AyudaLoginInitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ayuda_login_init);
		
		//OnClick de boton
		Button	botonConfig = (Button) findViewById(R.id.buttonIRSetURLLogin);
		botonConfig.setOnClickListener(new OnClickListener() {
		       public void onClick(View v) {
			          Intent intent = new Intent(AyudaLoginInitActivity.this, LoginSetDomainActivity.class);
			          startActivity(intent);
			       }
				});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ayuda_login_init, menu);
		return true;
	}

}
