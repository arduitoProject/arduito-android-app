package com.unlam.arduito.accesos;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.unlam.arduito.R;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.json.JSONParser;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class AccesosSimpleItemActivity  extends ListActivity {
	

	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	//url de datos del server
	private static String url1 = Constantes.URL_ACCESOS_POR_ID_1;
	private static String url2 = Constantes.URL_ACCESOS_POR_ID_2;
	
	// JSON node keys
	private static final String TAG_ACCESOS = "accesos";
	private static final String TAG_FECHA = "fecha";
	private static final String TAG_TARJETA = "tarjeta";
	private static final String TAG_PERMISO = "resultado";
	private static final String TAG_ID = "id";
	private static final String TAG_ID_LECTOR = "id_lector";
	
	private String id_sensor = "0";
	
	// contacts JSONArray
	JSONArray accesos = null;
	
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accesos_por_rfid);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        
        id_sensor = in.getStringExtra(TAG_ID_LECTOR);
        
        new GetAccesosDeRFID().execute();

    }	
	
	
	private class GetAccesosDeRFID extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
		
		
		
		
		ProgressDialog progressDialog;
		
		
		    @Override
		    protected void onPreExecute()
		    {
		        progressDialog= ProgressDialog.show(AccesosSimpleItemActivity.this, null,"Chequeando ultimos accesos", true);            
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
				 accesos = json.getJSONArray(TAG_ACCESOS);
				
				// looping through All Contacts
				for(int i = 0; i < accesos.length(); i++){
					JSONObject c = accesos.getJSONObject(i);
					
					// Storing each json item in variable
					String fecha = c.getString(TAG_FECHA);
					String tarjeta = c.getString(TAG_TARJETA);
					String permiso = c.getString(TAG_PERMISO);
					String permiso_editado = "no permitido";
					// Phone number is other JSON Object
					//JSONObject habitacion = c.getJSONObject(TAG_HABITACION);
					//String direccion =  habitacion.getString(TAG_HABITACION_DIRECCION) + " (Habitacion nro: " +habitacion.getString(TAG_HABITACION_NRO)+ " - piso: " + habitacion.getString(TAG_HABITACION_PISO) + " )";									
					
					if("null".equals(tarjeta)){
						tarjeta="no registrada";
					}
					
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					
					// adding each child node to HashMap key => value
					map.put(TAG_FECHA, fecha);
					map.put(TAG_TARJETA, tarjeta);
					
					if("true".equals(permiso)){
						permiso_editado = "permitido";
					}
					
					map.put(TAG_PERMISO, permiso_editado);

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
					R.layout.accesos_por_rfid_list_item,
					new String[] { TAG_FECHA, TAG_TARJETA, TAG_PERMISO }, new int[] {
							R.id.fecha, R.id.tarjeta_valor, R.id.acceso_value });

			setListAdapter(adapter);

			// selecting single ListView item
			ListView lv = getListView();


			
			
			//deshabilitar barra progreso
			super.onPostExecute(results);
	        progressDialog.dismiss();
	        
	        if(results != null && results.size() <= 0){
	        	Toast.makeText(AccesosSimpleItemActivity.this, "No se obtuvo información para listar" , Toast.LENGTH_LONG).show();
	        }
			
		}
		  
		  @Override
		protected void onCancelled() {
			Toast.makeText(AccesosSimpleItemActivity.this, "Error: La conexion no es la adecuada para recuperar los datos (cod 1001-M)" , Toast.LENGTH_SHORT).show();
			super.onCancelled();
		}
		
    }

}
