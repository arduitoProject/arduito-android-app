package com.unlam.arduito.configuracion;

import com.unlam.arduito.R;
import com.unlam.arduito.R.layout;
import com.unlam.arduito.R.menu;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class ConfiguracionKeyActivity extends Activity {
	
	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion_key);
		
		SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
		String value = "no-encontrado";
		
		
		TextView txtCambiado = (TextView)findViewById(R.id.textView2ConfigKey);
		
		try {
			value = sharedPreferences.getValue(context, "keyGCM");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if("no-encontrado".equals(value)){
			txtCambiado.setText("No existe código para el dispositivo");
		}else{
			txtCambiado.setText("código: " + value);	
		}
		
		   
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuracion_key, menu);
		return true;
	}

}
