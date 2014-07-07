package com.unlam.arduito.configuracion;

import com.unlam.arduito.MainActivity;
import com.unlam.arduito.R;
import com.unlam.arduito.R.layout;
import com.unlam.arduito.R.menu;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.sensores.SensoresActivity;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfiguracionGeneralActivity extends Activity {

	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	EditText editServer;
	EditText editUserCam;
	EditText editPassCam;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion_general);
		
		String url_server = "";
		String user_cam = "";
		String pass_cam = "";
		
		
		try {
			url_server = sharedPreferences.getDominio(context);
			user_cam = sharedPreferences.getValue(context, "camara_user");
			pass_cam = sharedPreferences.getValue(context, "camara_pass");
		} catch (ServiceException e) {
			e.printStackTrace();
		}


		if( user_cam == null || user_cam == "no-encontrado" || user_cam == ""){
			user_cam = Constantes.USER_CAM;
		}

		if( pass_cam == null || pass_cam == "no-encontrado" || pass_cam == ""){
			pass_cam = Constantes.PASS_CAM;
		}
		
		editServer = (EditText) findViewById(R.id.editTextHOST);
		editServer.setText(url_server);
		
		editUserCam = (EditText) findViewById(R.id.editTextCAMARA);
		editUserCam.setText(user_cam);

		editPassCam = (EditText) findViewById(R.id.editTextPass);
		editPassCam.setText(pass_cam);
		
		
		
		//OnClick de boton sensores
		Button	botonGuardar = (Button) findViewById(R.id.buttonGUARDAR);
			botonGuardar.setOnClickListener(new OnClickListener() {
				       public void onClick(View v) {
				    	   mostrarAlertDialog(editServer.getText().toString(), editUserCam.getText().toString(), editPassCam.getText().toString());
				       }
				});
		
		Button	botonCancelar = (Button) findViewById(R.id.buttonCANCELAR);
			botonCancelar.setOnClickListener(new OnClickListener() {
				       public void onClick(View v) {
				          Intent intent = new Intent(ConfiguracionGeneralActivity.this, MainActivity.class);
				          startActivity(intent);
				       }
				});		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuracion_general, menu);
		return true;
	}
	
	
	
	private boolean isURLvalida(String url_server){
		
		if( !URLUtil.isValidUrl(url_server)){
			return false;
		}
		return true;
	}
	
	
	private void mostrarAlertDialog(String url_server, String user, String pass){

		//esto lo hago para no meter espacios sin querer
		url_server = url_server.replaceAll(" ", "");
		user = user.replaceAll(" ", "");
		pass = pass.replaceAll(" ", "");
		
		if(!isURLvalida(url_server)){
			Toast.makeText(getApplicationContext(),"Las URLs ingresadas es incorrecta",Toast.LENGTH_LONG).show();
		}else{
		
		
		final String url_nueva_server = url_server;
		final String usuario_cam = user;
		final String password_cam = pass;
		
		
		//inicio el alertDialog	
    	AlertDialog.Builder alert = new AlertDialog.Builder(context);
    	
    	
    		alert.setTitle("Confirmar acción");
    		alert.setMessage("Esta por modificar datos sensibles, si esta seguro confirme la acción. El cambio podría inutilizar la aplicación." );


    	//seteo boton OK
	    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int whichButton) {
	    		setDominioEnShared(url_nueva_server, usuario_cam, password_cam );
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
    }
	
	
	private void setDominioEnShared(String url_server, String user_cam, String pass_cam){
		
		try {
			sharedPreferences.setValue(context, "url_servidor", url_server);
			sharedPreferences.setValue(context, "camara_user", user_cam);
			sharedPreferences.setValue(context, "camara_pass", pass_cam);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
