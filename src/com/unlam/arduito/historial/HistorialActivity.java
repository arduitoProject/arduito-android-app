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
import com.unlam.arduito.accesos.AccesosSimpleItemActivity;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.json.JSONParser;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class HistorialActivity extends ListActivity {	
	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	// JSON Node names	
	private static final String TAG_SENSORES = "sensores";
	private static final String TAG_WARNING = "warning";
	private static final String TAG_TIPO = "tipo";
	private static final String TAG_ID = "id";
	private static final String TAG_NRO_HABITACION = "numeroHabitacion";
	private static final String TAG_DIRECCION = "direccion";
	private static final String TAG_FUERA_RANGO = "fueraRango";
	
	
	
	//{"warning":23,"fueraRango":1,"id":24,"tipo":"polvo","numeroHabitacion":"1","direccion":"Reconquista 464"

	// contacts JSONArray
	JSONArray contacts = null;

	
	
	private class GetHistorial extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
		
		ProgressDialog progressDialog;
		
		
		    @Override
		    protected void onPreExecute()
		    {
		        progressDialog= ProgressDialog.show(HistorialActivity.this, null,"Chequeando informe historial", true);            
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

			String url = dominio + Constantes.URL_ALERTAS_1 + pin + Constantes.URL_ALERTAS_2 ; 
			
			
			
			// Hashmap for ListView
			ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url);

			try {
				
				if(json!=null){
						// Getting Array of Contacts
						contacts = json.getJSONArray(TAG_SENSORES);
						
						// looping through All Contacts
						for(int i = 0; i < contacts.length(); i++){
							JSONObject c = contacts.getJSONObject(i);
							
							// Storing each json item in variable
							//{"warning":23,"fueraRango":1,"id":24,"tipo":"polvo","numeroHabitacion":"1","direccion":"Reconquista 464"
							String cant_warn = c.getString(TAG_WARNING);
							String cant_fuera_rango = c.getString(TAG_FUERA_RANGO);
							String id = c.getString(TAG_ID);
							String tipo = c.getString(TAG_TIPO);
							String nro_habitacion = c.getString(TAG_NRO_HABITACION);
							String direccion = c.getString(TAG_DIRECCION);
							
							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();
							
							// adding each child node to HashMap key => value
							map.put(TAG_TIPO, tipo);
							map.put(TAG_ID, id);
							map.put(TAG_FUERA_RANGO, cant_fuera_rango);
							map.put(TAG_WARNING, cant_warn);
							map.put(TAG_NRO_HABITACION, nro_habitacion);
							map.put(TAG_DIRECCION, direccion);
		
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
					R.layout.historial_list_item,
					new String[] { TAG_TIPO, TAG_ID, TAG_DIRECCION, TAG_NRO_HABITACION, TAG_FUERA_RANGO, TAG_WARNING}, new int[] {
							R.id.name, R.id.idSensorOculto, R.id.ubicacion, R.id.nroHabitacion, R.id.valor, R.id.valor_warn });

			setListAdapter(adapter);

			// selecting single ListView item
			ListView lv = getListView();

			// Launching new screen on Selecting Single ListItem
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// getting values from selected ListItem
					String name = ((TextView) view.findViewById(R.id.idSensorOculto)).getText().toString();
					//String cost = ((TextView) view.findViewById(R.id.email)).getText().toString();
					//String description = ((TextView) view.findViewById(R.id.mobile)).getText().toString();
					
					// Starting new intent
					Intent in = new Intent(getApplicationContext(), HistorialSimpleItemActivity.class);
					in.putExtra(TAG_ID, name);
					//in.putExtra(TAG_EMAIL, cost);
					//in.putExtra(TAG_PHONE_MOBILE, description);
					startActivity(in);

				}
			});
			
			
			//deshabilitar barra progreso
			super.onPostExecute(results);
	        progressDialog.dismiss();
			
	        if(results != null && results.size() <= 0){
	        	Toast.makeText(HistorialActivity.this, "No se obtuvo información para listar" , Toast.LENGTH_LONG).show();
	        }
	        
	        
		}
		  
		  @Override
		protected void onCancelled() {
			Toast.makeText(HistorialActivity.this, "Error: La conexion no es la adecuada para recuperar los datos (cod 1002-M)" , Toast.LENGTH_SHORT).show();
			super.onCancelled();
		}
		
    }
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historial);
		new GetHistorial().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.historial, menu);
		return true;
	}

}
