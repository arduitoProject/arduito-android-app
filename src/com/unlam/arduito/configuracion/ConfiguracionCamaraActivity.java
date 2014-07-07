package com.unlam.arduito.configuracion;

import com.unlam.arduito.R;
import com.unlam.arduito.R.id;
import com.unlam.arduito.R.layout;
import com.unlam.arduito.R.menu;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class ConfiguracionCamaraActivity extends Activity {

	//context
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion_camara);
		
		
		
		
		CheckBox check = (CheckBox) findViewById(R.id.checkBoxSizeCamara);
		 
		
		String estaTildado = ""; 
		try {
			estaTildado = sharedPreferences.getValue(context, "camara_full_size");
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		
		if("YES".equals(estaTildado)){
			check.setChecked(true);
		}else{
			check.setChecked(false);
		}
		
		
		
		
		
		//es el listener del check de tamaño de visualizcion cámara
		check.setOnClickListener(new OnClickListener() {
	 
		  @Override
		  public void onClick(View v) {
			  
			  String valor = "NO";
			//si quedo chequeado pongo la property en uno  
			if (((CheckBox) v).isChecked()) {
			      valor= "YES";	
			}
	 
			//seteo el preferences	
			try {
					sharedPreferences.setValue(context, "camara_full_size", valor);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
		  }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuracion_camara, menu);
		return true;
	}

}
