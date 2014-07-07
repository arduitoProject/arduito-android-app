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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.unlam.arduito.R;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.json.JSONParser;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class VerAccesosActivity extends ListActivity {

	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	
	// url to make request
	//private static String url = "http://api.androidhive.info/contacts/";
	private static String url1 = Constantes.URL_TODOS_ACCESOS_1;
	private static String url2 = Constantes.URL_TODOS_ACCESOS_2;
	
	private static final String TAG_ACCESOS = "accesos";
	private static final String TAG_ID_LECTOR = "id_lector";
	private static final String TAG_VALIDOS = "cant_accesos_validos_hoy";
	private static final String TAG_NO_VALIDOS = "cant_accesos_no_validos_hoy";
	private static final String TAG_HABITACION = "habitacion";
	private static final String TAG_HABITACION_ID = "id";
	private static final String TAG_HABITACION_DIRECCION = "direccion";
	private static final String TAG_HABITACION_NRO = "numero";
	private static final String TAG_HABITACION_PISO = "piso";
	
//	{"accesos":[{"id_lector":1,"cant_accesos_validos_hoy":0,"cant_accesos_no_validos_hoy":0,"habitacion":{"id":1,"direccion":"aaa","numero":"1","piso":"1"}}]}
	
	

	// contacts JSONArray
	JSONArray accesos = null;

	
	
	private class GetAccesos extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
		
		
		
		
		ProgressDialog progressDialog;
		
		
		    @Override
		    protected void onPreExecute()
		    {
		        progressDialog= ProgressDialog.show(VerAccesosActivity.this, null,"Chequeando lectores de tarjetas", true);            
		    };
				
		
		
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
			
			
			String pin = "111";
			String dominio = Constantes.dominio;
			try {
				dominio = sharedPreferences.getDominio(context);
				pin = sharedPreferences.getValue(context, "pin");
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
			
			String url = dominio + url1 + pin + url2;
			
			// Hashmap for ListView
			ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url);

			try {
				
				if(json!=null){
						// Getting Array of Contacts
						 accesos = json.getJSONArray(TAG_ACCESOS);
						
						// looping through All Contacts
						for(int i = 0; i < accesos.length(); i++){
							JSONObject c = accesos.getJSONObject(i);
							
							// Storing each json item in variable
							String id = c.getString(TAG_ID_LECTOR);
							String cant_validos = c.getString(TAG_VALIDOS);
							String cant_invalidos = c.getString(TAG_NO_VALIDOS);
		
							// Phone number is other JSON Object
							JSONObject habitacion = c.getJSONObject(TAG_HABITACION);
							String direccion =  habitacion.getString(TAG_HABITACION_DIRECCION) + " (Hab.: " +habitacion.getString(TAG_HABITACION_NRO)+ " - piso: " + habitacion.getString(TAG_HABITACION_PISO) + " )";									
							
							
							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();
							
							// adding each child node to HashMap key => value
							map.put(TAG_ID_LECTOR, id);
							map.put(TAG_HABITACION_DIRECCION, direccion);
							map.put("VALORES", cant_validos + " VALIDOS / " + cant_invalidos + " INVALIDOS");
		
							// adding HashList to ArrayList
							contactList.add(map);
						}
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
					R.layout.accesos_list_item,
					new String[] { TAG_ID_LECTOR, TAG_HABITACION_DIRECCION, "VALORES" }, new int[] {
							R.id.sensor_acceso_nro, R.id.ubicacion_accesos, R.id.accesos_valores });

			setListAdapter(adapter);

			// selecting single ListView item
			ListView lv = getListView();

			// Launching new screen on Selecting Single ListItem
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// getting values from selected ListItem
					String id_2 = ((TextView) view.findViewById(R.id.sensor_acceso_nro)).getText().toString();
					
					// Starting new intent
					Intent in = new Intent(getApplicationContext(), AccesosSimpleItemActivity.class);
					in.putExtra(TAG_ID_LECTOR, id_2);
					startActivity(in);

				}
			});
			
			
			//deshabilitar barra progreso
			super.onPostExecute(results);
	        progressDialog.dismiss();
	        
	        if(results != null && results.size() <= 0){
	        	Toast.makeText(VerAccesosActivity.this, "No se obtuvo información para listar" , Toast.LENGTH_LONG).show();
	        }
			
		}
		  
		  @Override
		protected void onCancelled() {
			Toast.makeText(VerAccesosActivity.this, "Error: La conexion no es la adecuada para recuperar los datos (cod 1001-M)" , Toast.LENGTH_SHORT).show();
			super.onCancelled();
		}
		
    }
	
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accesos);
		new GetAccesos().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ver_accesos, menu);
		return true;
	}

}
