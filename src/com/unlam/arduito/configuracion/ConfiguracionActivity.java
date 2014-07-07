package com.unlam.arduito.configuracion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unlam.arduito.R;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class ConfiguracionActivity extends Activity {

	private Context context = this;
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion);
		
		
		/************	inicio botones *****************************/
		
		Button	botonkey = (Button) findViewById(R.id.btnConfigKey);
		botonkey.setOnClickListener(new OnClickListener() {
				      public void onClick(View v) {
				          Intent intent = new Intent(ConfiguracionActivity.this, ConfiguracionKeyActivity.class);
				          startActivity(intent);
				       }
				 });
		
		Button	botonConfigGral = (Button) findViewById(R.id.btnConfigGRAL);
		botonConfigGral.setOnClickListener(new OnClickListener() {
				      public void onClick(View v) {
				          Intent intent = new Intent(ConfiguracionActivity.this, ConfiguracionGeneralActivity.class);
				          startActivity(intent);
				       }
				 });
		
		Button	botonConfigCam = (Button) findViewById(R.id.btnConfiguracionCamara);
		botonConfigCam.setOnClickListener(new OnClickListener() {
				      public void onClick(View v) {
				          Intent intent = new Intent(ConfiguracionActivity.this, ConfiguracionCamaraActivity.class);
				          startActivity(intent);
				       }
				 });

		Button	botonResetUsuario = (Button) findViewById(R.id.btnConfigResetarUsuaario);
		botonResetUsuario.setOnClickListener(new OnClickListener() {
				      public void onClick(View v) {
				          Intent intent = new Intent(ConfiguracionActivity.this, ConfigurarResetUsuarioActivity.class);
				          startActivity(intent);
				       }
				 });
		
		Button	botonsolicitarNoti = (Button) findViewById(R.id.buttonPedirNotificacionPrueba);
		botonsolicitarNoti.setOnClickListener(new OnClickListener() {
				      public void onClick(View v) {
				          Intent intent = new Intent(ConfiguracionActivity.this, TestNotificacionActivity.class);
				          startActivity(intent);
				       }
				 });
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuracion, menu);
		return true;
	}
	
	
	
	
	private void mostrarAlertDialogPin(){
		
		//inicio el alertDialog	
    	AlertDialog.Builder alert = new AlertDialog.Builder(context);
    	
    	String val = "no-encontrado";
    	try {
			val = sharedPreferences.getValue(context, "pin");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	
    	if("no-encontrado".equals(val)){
    		alert.setTitle("No se detecto PIN asociado");
    		alert.setMessage("Ingrese su PIN (4 digitos)");
    	}else{
    		alert.setTitle("Atención! está por modificar su PIN: " + val );
    		alert.setMessage("Ingrese su nuevo PIN (4 digitos)");
    	}

    	

        //seteo inicial del primer edit text 
        final EditText input = new EditText(context);
        alert.setView(input);

    	//seteo boton OK
	    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int whichButton) {
	    	 String srt = input.getEditableText().toString();
		    	
		    		 Toast.makeText(getApplicationContext(),"Reseteando validacion de usuario",Toast.LENGTH_LONG).show();
		    		 
		    		 
		    		resetearValidacionUsuario();
		    	 
	    	} 
	      });
        
        //seteo boton CANCEL
    	alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {
    		  dialog.cancel();
    		  Toast.makeText(getApplicationContext(),"Modificacion cancelada",Toast.LENGTH_LONG).show();
    	  }
    	}); 
    	
    	AlertDialog alertDialog = alert.create();
    	alertDialog.show();
	}

	
	private void resetearValidacionUsuario(){
		
		try {
			sharedPreferences.setValue(context, "esta_validado", "NO");
			sharedPreferences.setValue(context, "pin", "no-encontrado");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
