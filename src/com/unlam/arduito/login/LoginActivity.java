package com.unlam.arduito.login;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unlam.arduito.MainActivity;
import com.unlam.arduito.R;
import com.unlam.arduito.ayuda.AyudaLoginInitActivity;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.json.JSONParser;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class LoginActivity extends Activity {

	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	//tags para el json
	private static final String TAG_RESULTADO = "resultado";
	private static final String TAG_PIN = "pin";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

	
		
		final EditText username=(EditText)findViewById(R.id.nombreUsuario_login);
		final EditText password=(EditText)findViewById(R.id.codigo_registro_login);

		
		//username.setText("ingrese nombre usuario");
		//username.setText("ingrese código de registro");
		
		//OnClick de boton de ayuda
		Button	botonAyuda = (Button) findViewById(R.id.boton_ayuda_login);
		botonAyuda.setOnClickListener(new OnClickListener() {
		       public void onClick(View v) {
		          Intent intent = new Intent(LoginActivity.this, AyudaLoginInitActivity.class);
		          startActivity(intent);
		       }
		});
		

		//OnClick de boton de ingresar
		Button	botonRegistro = (Button) findViewById(R.id.boton_verificar_login);
		botonRegistro.setOnClickListener(new OnClickListener() {
		       public void onClick(View v) {
		          if(validarDatos(username.getText().toString(), password.getText().toString()) && isOnline()){
		        	  new VerificarLogin().execute(username.getText().toString(), password.getText().toString());
		          }
		       }
		});
		
		
		
		/*
		 Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        	startActivity(intent); 
		 
		 */
		
	}

	
	private boolean validarDatos(String usr, String codigo){
		
		if(usr != null && codigo !=null && !"Código de registro".equals(codigo) && !"Nombre de usuario".equals(usr)){
			if(usr.length() > 3 && usr.length() < 30 && codigo.length() > 3 && codigo.length() < 30){
				return true;
			}
		}
		Toast.makeText(LoginActivity.this, "Los datos ingresados son incorrectos (90-M)" , Toast.LENGTH_LONG).show();
		return false;
		
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm = 
	         (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    
		Toast.makeText(LoginActivity.this, "No es posible verificar sus datos en este momento ya que el dispositivo no dispone de conexión a internet. (codigo: 91-M)" , Toast.LENGTH_LONG).show();
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	
	
	
	
	
	//INICIO DE TAREA ASYNC
	private class VerificarLogin extends AsyncTask<String, Void, ArrayList<String>> {
		
		ProgressDialog progressDialog;
		
		
		    @Override
		    protected void onPreExecute()
		    {
		        progressDialog= ProgressDialog.show(LoginActivity.this, null,"Verificando su registro, por favor espere.", true);            
		    };
				
		
		
		@Override
		protected ArrayList<String> doInBackground(String... params) {
			
			ArrayList<String> valores = new ArrayList<String>();
			String validado = "no";
			String pin = "no";
			String usuario = params[0];
			String codigo_registro = params[1];
			
			
			String dominio = Constantes.dominio;
			try {
				dominio = sharedPreferences.getDominio(context);
			} catch (ServiceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String url = dominio + Constantes.URL_VALIDAR_LOGIN + codigo_registro + "/" + usuario;

			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url);

			try {
				
				  if(json!=null){
					  			validado =json.getString(TAG_RESULTADO);
					  			if("si".equals(validado)){
					  				pin =json.getString(TAG_PIN);
					  			}
				  }
						
			} catch (JSONException e) {
				Toast.makeText(LoginActivity.this, "Existe un error al comunicarse con el servidor" , Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
			valores.add(validado);
			valores.add(usuario);
			valores.add(pin);
			return valores;
			
		}
		
		
		protected void onPostExecute(ArrayList<String> result) {

			String habilitado = result.get(0);
			String usuario = result.get(1);
			String pin = result.get(2);
			
			//si esta habilitado guardo el pin en el shared y lo uso de ahora en adelante.
			if("si".equals(habilitado)){
				
				try {
					sharedPreferences.setValue(context, "esta_validado", "YES");
					sharedPreferences.setValue(context, "pin", pin);
					sharedPreferences.setValue(context, "usuario", usuario);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//deshabilitar barra progreso
				super.onPostExecute(result);
		        progressDialog.dismiss();
		        
		        Toast.makeText(LoginActivity.this, "La validación fue exitosa, bienvenidos a arduito móvil" , Toast.LENGTH_SHORT).show();
		        
		        // Starting new intent
				Intent in = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(in);
			}else{
				//deshabilitar barra progreso
				super.onPostExecute(result);
		        progressDialog.dismiss();
				//username.setText("ingrese nombre usuario");
				//username.setText("ingrese código de registro");
				Toast.makeText(LoginActivity.this, "No es posible autentificar los datos (cod 100-M)" , Toast.LENGTH_LONG).show();
			}
							// Starting new intent
							//Intent in = new Intent(getApplicationContext(), SensoresSimpleItemActivity.class);
							//startActivity(in);

			//deshabilitar barra progreso
			//super.onPostExecute(result);
	        //progressDialog.dismiss();
			
		}
		  
		  @Override
		protected void onCancelled() {
			Toast.makeText(LoginActivity.this, "Error: La conexion no es la adecuada para recuperar los datos (cod 1000-M)" , Toast.LENGTH_SHORT).show();
			super.onCancelled();
		}
		
    }
	
}
