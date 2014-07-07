package com.unlam.arduito.login;

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

public class LoginSetDomainActivity extends Activity {

	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	EditText editServer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_set_domain);
		
		String url_server = "";

		
		try {
			url_server = sharedPreferences.getDominio(context);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		editServer = (EditText) findViewById(R.id.editTextHOST_login);
		editServer.setText(url_server);
		
		
		
		//OnClick de boton sensores
		Button	botonGuardar = (Button) findViewById(R.id.buttonGUARDAR_login);
			botonGuardar.setOnClickListener(new OnClickListener() {
				       public void onClick(View v) {
				    	   mostrarAlertDialog(editServer.getText().toString());
				       }
				});
		
		Button	botonCancelar = (Button) findViewById(R.id.buttonCANCELAR_login);
			botonCancelar.setOnClickListener(new OnClickListener() {
				       public void onClick(View v) {
				          Intent intent = new Intent(LoginSetDomainActivity.this, LoginActivity.class);
				          startActivity(intent);
				       }
				});		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_set_domain, menu);
		return true;
	}
	
	
	private void mostrarAlertDialog(String url_server){

		
		//esto lo hago para no meter espacios sin querer
		url_server = url_server.replaceAll(" ", "");
		
		final String url_nueva_server = url_server;
		//inicio el alertDialog	
    	AlertDialog.Builder alert = new AlertDialog.Builder(context);
    	
    	
    		alert.setTitle("Confirmar acción");
    		alert.setMessage("Esta por modificar la url del servidor, si esta seguro confirme la acción." );


    	//seteo boton OK
	    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int whichButton) {
	    		setDominioEnShared(url_nueva_server);
	    		Toast.makeText(getApplicationContext(),"El cambio fue aplicado correctamente",Toast.LENGTH_LONG).show();
	    	} 
	      });
        
        //seteo boton CANCEL
    	alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {
    		  dialog.cancel();
    		  Toast.makeText(getApplicationContext(),"El cambio fue cancelado",Toast.LENGTH_LONG).show();
    	  }
    	}); 
    	
    	AlertDialog alertDialog = alert.create();
    	alertDialog.show();
    }
	
	
	private void setDominioEnShared(String url_server){
		
		try {
			sharedPreferences.setValue(context, "url_servidor", url_server);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
