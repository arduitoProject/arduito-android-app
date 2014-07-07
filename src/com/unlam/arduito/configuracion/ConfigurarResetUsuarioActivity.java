package com.unlam.arduito.configuracion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.unlam.arduito.R;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class ConfigurarResetUsuarioActivity extends Activity {
	
	private Context context = this;
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configurar_reset_usuario);
		
		
		Button	botonReset = (Button) findViewById(R.id.buttonConfigResetUsuario);		
		botonReset.setOnClickListener(new OnClickListener() {
		       public void onClick(View v) {
		    	   mostrarAlertDialogPin();
		       }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configurar_reset_usuario, menu);
		return true;
	}
	
	
	private void mostrarAlertDialogPin(){
		
		//inicio el alertDialog	
    	AlertDialog.Builder alert = new AlertDialog.Builder(context);
    	
    	String val = "no-encontrado";
    	try {
			val = sharedPreferences.getValue(context, "usuario");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	
    	if("no-encontrado".equals(val)){
    		alert.setTitle("Reinicio del usuario validado");
    		alert.setMessage("Esta por reiniciar el usuario validado asociado al dispositivo. Al seleccionar OK se hará efectivo el borrado de la validación actual, se cerrará la aplicación y será necesario una nueva validación" );
    	}else{
    		alert.setTitle("Reinicio del usuario validado");
    		alert.setMessage("Esta por reiniciar el usuario " +val+ " validado y asociado al dispositivo. Al seleccionar OK se hará efectivo el borrado de la validación actual y será necesario una nueva validación" );
    	}

    	//seteo boton OK
	    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int whichButton) {
	    	 
	    		resetearValidacionUsuario();
	    		Toast.makeText(getApplicationContext(),"El usuario fue reiniciado correctamente.",Toast.LENGTH_LONG).show();
	    		/*
	    		ProgressDialog progressDialog;
			    progressDialog= ProgressDialog.show(ConfigurarResetUsuarioActivity.this, null,"Reiniciando usuario y cerrando aplicación", true);            
			    
			    try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    progressDialog.dismiss();
			    
			    //cierra la aplicacion
			    System.exit(0);
			    */
	    	} 
	      });
        
        //seteo boton CANCEL
    	alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {
    		  dialog.cancel();
    		  Toast.makeText(getApplicationContext(),"Reinicio de usuario cancelado",Toast.LENGTH_LONG).show();
    	  }
    	}); 
    	
    	AlertDialog alertDialog = alert.create();
    	alertDialog.show();
	}
	
	
	private void resetearValidacionUsuario(){
		
		try {
			sharedPreferences.setValue(context, "esta_validado", "NO");
			sharedPreferences.setValue(context, "pin", "no-encontrado");
			sharedPreferences.setValue(context, "keyGCM", "reseteada");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
