package com.unlam.arduito.historial;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.unlam.arduito.R;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.json.JSONParser;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class HistorialSimpleItemActivity extends ListActivity {
	

	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	//url de datos del server
	private static String url1 = Constantes.URL_ALERTAS_1;
	private static String url2 = Constantes.URL_ALERTAS_2;
	
	// JSON node keys
	private static final String TAG_VALORES = "valores";
	private static final String TAG_FECHA = "fechaCreacion";
	private static final String TAG_RESULTADO = "resultado";
	private static final String TAG_ID = "id";
	private static final String TAG_VALOR_MEDIDO = "valor";
	
	private String id_sensor = "0";
	
	// contacts JSONArray
	JSONArray accesos = null;
	
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_single_list_item);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        
        id_sensor = in.getStringExtra(TAG_ID);
        
        new GetUltimos10ALertas().execute();

    }	
	
	
	private class GetUltimos10ALertas extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
		
		
		
		
		ProgressDialog progressDialog;
		
		
		    @Override
		    protected void onPreExecute()
		    {
		        progressDialog= ProgressDialog.show(HistorialSimpleItemActivity.this, null,"Chequeando ultimas alertas (Cantidad máxima 10 registros)", true);            
		    };
				
		
		
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
			
			String dominio = Constantes.dominio;
			String pin = "111";
			try {
				dominio = sharedPreferences.getDominio(context);
				pin = sharedPreferences.getValue(context, "pin");
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
			
			String url = dominio + url1 + pin + url2 + id_sensor;
			
			// Hashmap for ListView
			ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url);

			try {
				// Getting Array of Contacts
				 accesos = json.getJSONArray(TAG_VALORES);
				
				// looping through All Contacts
				for(int i = 0; i < accesos.length(); i++){
					JSONObject c = accesos.getJSONObject(i);
					
					// Storing each json item in variable
					String fecha = c.getString(TAG_FECHA);
					String resultado = c.getString(TAG_RESULTADO);
					String valor = c.getString(TAG_VALOR_MEDIDO);
					
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					
					// adding each child node to HashMap key => value
					map.put(TAG_FECHA, fecha);
					map.put(TAG_RESULTADO, resultado);
					map.put(TAG_VALOR_MEDIDO, valor);

					// adding HashList to ArrayList
					contactList.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return contactList;
			
		}
		
         
		protected void onPostExecute(ArrayList<HashMap<String, String>> results) {
			
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(getApplicationContext(), results,
					R.layout.historial_single_item,
					new String[] { TAG_FECHA, TAG_RESULTADO, TAG_VALOR_MEDIDO}, new int[] {
							R.id.fecha, R.id.medicion_value, R.id.valor_medido});

			setListAdapter(adapter);

			// selecting single ListView item
			//ListView lv = getListView();


			
			
			//deshabilitar barra progreso
			super.onPostExecute(results);
	        progressDialog.dismiss();
	        
	        if(results != null && results.size() <= 0){
	        	Toast.makeText(HistorialSimpleItemActivity.this, "No se obtuvo información para listar" , Toast.LENGTH_LONG).show();
	        }
			
		}
		  
		  @Override
		protected void onCancelled() {
			Toast.makeText(HistorialSimpleItemActivity.this, "Error: La conexion no es la adecuada para recuperar los datos (cod 1001-M)" , Toast.LENGTH_SHORT).show();
			super.onCancelled();
		}
		
    }

}
