package com.unlam.arduito.configuracion;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.unlam.arduito.R;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.json.JSONParser;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class TestNotificacionActivity extends Activity {

	private Context context = this;
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	private static String url1 = Constantes.URL_TEST_1;
	private static String url2 = Constantes.URL_TEST_2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_notificacion);
	
	
		//OnClick de boton test
		Button	botonUltimosAccesos = (Button) findViewById(R.id.buttonTestNotification);
		botonUltimosAccesos.setOnClickListener(new OnClickListener() {
		       public void onClick(View v) {
		    	   new GetNotificacionPrueba().execute();
		       }
		});
	
	
	}

	// contacts JSONArray
	JSONArray resultado = null;
	
	
	private class GetNotificacionPrueba extends AsyncTask<Void, Void, ArrayList<String>> {
		
		
		
		
		ProgressDialog progressDialog;
		
		
		@Override
		protected void onPreExecute()
		{
			progressDialog= ProgressDialog.show(TestNotificacionActivity.this, null,"Solicitando envío de notificacion de prueba", true);            
		};
		
		
		
		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			
			String dominio = Constantes.dominio;
			String pin = "111";
			try {
				dominio = sharedPreferences.getDominio(context);
				pin = sharedPreferences.getValue(context, "pin");
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
			
			ArrayList<String> valores = new ArrayList<String>();
			String url = dominio + url1 + pin + url2;
			String envio = "no";
			String msg = "sin-mensaje";			
			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();
			
			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url);
			
			try {
				
				  if(json!=null){
					  			envio = json.getString("resultado");
					  			if("no".equals(envio)){
					  				msg =json.getString("mensaje");
					  			}
				  }
						
			} catch (JSONException e) {
				Toast.makeText(TestNotificacionActivity.this, "Existe un error al comunicarse con el servidor" , Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
			valores.add(envio);
			valores.add(msg);
			
			return valores;
			
		}	
		
		
		protected void onPostExecute(ArrayList<String> results) {

			String res1 = results.get(0);
			String res2 = results.get(1);
			
			//deshabilitar barra progreso
			super.onPostExecute(results);
			progressDialog.dismiss();
			
			if("ok".equals(res1)){
				Toast.makeText(TestNotificacionActivity.this, "Listo! en breve recibiras una notificación de prueba" , Toast.LENGTH_LONG).show();
			}else if("no".equals(res1)){
				Toast.makeText(TestNotificacionActivity.this, "La solicitud no ha podido resolverse correctamente (cod. 1003-M)", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(TestNotificacionActivity.this, "La solicitud no ha podido resolverse correctamente (cod. 1003-M)" , Toast.LENGTH_LONG).show();
			}
			
		}
		
		@Override
		protected void onCancelled() {
			Toast.makeText(TestNotificacionActivity.this, "Error: La conexion no es la adecuada para recuperar los datos (cod 1001-M)" , Toast.LENGTH_SHORT).show();
			super.onCancelled();
		}
		
	}
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_notificacion, menu);
		return true;
	}

}
