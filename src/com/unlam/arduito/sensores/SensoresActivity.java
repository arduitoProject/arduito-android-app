package com.unlam.arduito.sensores;

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

public class SensoresActivity extends ListActivity {
	
	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	private boolean error = false;
	
	// JSON Node names
/*
	private static final String TAG_CONTACTS = "contacts";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_GENDER = "gender";
	private static final String TAG_PHONE = "phone";
	private static final String TAG_PHONE_MOBILE = "mobile";
	private static final String TAG_PHONE_HOME = "home";
	private static final String TAG_PHONE_OFFICE = "office";
*/

	//{"id":1,"habitacion":{"id":2,"direccion":"aaa","numero":"1","piso":"1"},"valorActual":20,"valorMaximo":38,"valorMinimo":5,"nombreSensor":"temperatura"},
	
	//Json de sensores
	private static final String TAG_SENSORES = "sensores";
	private static final String TAG_ID = "id";
	private static final String TAG_HABITACION = "habitacion";
	private static final String TAG_HABITACION_ID = "id";
	private static final String TAG_HABITACION_DIRECCION = "direccion";
	private static final String TAG_HABITACION_NRO = "numero";
	private static final String TAG_HABITACION_PISO = "piso";
	private static final String TAG_VALOR_ACTUAL = "valorActual";
	private static final String TAG_VALOR_MIN = "valorMinimo";
	private static final String TAG_VALOR_MAX = "valorMaximo";	
	private static final String TAG_RANGO = "rango";	
	private static final String TAG_NAME = "nombreSensor";
	private static final String TAG_UNIDAD = "unidad";
	
	
	// contacts JSONArray
	JSONArray contacts = null;

	
	
	private class GetEstadoSensores extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
		
		ProgressDialog progressDialog;
		
		
		    @Override
		    protected void onPreExecute()
		    {
		        progressDialog= ProgressDialog.show(SensoresActivity.this, null,"Chequeando estado sensores", true);            
		    };
				
		
		
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
			
			String dominio = "";
			String pin = "111";
			try {
				dominio = sharedPreferences.getDominio(context);
				pin = sharedPreferences.getValue(context, "pin");
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}

			String url = dominio + Constantes.URL_SENSORES_1 + pin + Constantes.URL_SENSORES_2; 
			
			
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
									String id_sensor = c.getString(TAG_ID);
									String name = c.getString(TAG_NAME);
									String valorActual = c.getString(TAG_VALOR_ACTUAL);
									String unidad = c.getString(TAG_UNIDAD);
									String min = c.getString(TAG_VALOR_MIN);
									String max = c.getString(TAG_VALOR_MAX);
									
									
									
									if(min == null || "".equals(min)){
										min = "0";
									}
									
									if(max == null || "".equals(max)){
										max = "0";
									}
									
									// Phone number is other JSON Object
									JSONObject habitacion = c.getJSONObject(TAG_HABITACION);
									String id_hab = habitacion.getString(TAG_HABITACION_ID);
									String direccion =  habitacion.getString(TAG_HABITACION_DIRECCION) + " (Hab.: " +habitacion.getString(TAG_HABITACION_NRO)+ " - piso: " + habitacion.getString(TAG_HABITACION_PISO) + " )";									
									
									// creating new HashMap
									HashMap<String, String> map = new HashMap<String, String>();
									
									// adding each child node to HashMap key => value
									//map.put(TAG_ID, id);
									map.put(TAG_NAME, name);
									map.put(TAG_VALOR_ACTUAL, valorActual);
									map.put(TAG_HABITACION_DIRECCION, direccion);
									map.put(TAG_UNIDAD, unidad);
									map.put(TAG_RANGO, "(normal: " +min+" a " + max + ")");
									map.put(TAG_ID, id_sensor);
				
									// adding HashList to ArrayList
									contactList.add(map);
								}
				  }else{
					  error = true;
				  }
						
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return contactList;
			
		}
		
		
		protected void onPostExecute(ArrayList<HashMap<String, String>> results) {
		
			if(!error){
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(getApplicationContext(), results,
							R.layout.sensores_list_item,
							new String[] { TAG_NAME, TAG_ID,TAG_HABITACION_DIRECCION, TAG_VALOR_ACTUAL,TAG_UNIDAD, TAG_RANGO }, new int[] {
									R.id.name,R.id.idSensorOculto,R.id.ubicacion, R.id.valor, R.id.unidad, R.id.minMaxValues});
		
					setListAdapter(adapter);
		
					// selecting single ListView item
					ListView lv = getListView();
		
					// Launching new screen on Selecting Single ListItem
					lv.setOnItemClickListener(new OnItemClickListener() {
		
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							
							// getting values from selected ListItem
							String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
							String unidad = ((TextView) view.findViewById(R.id.unidad)).getText().toString();
							String id_sensor = ((TextView) view.findViewById(R.id.idSensorOculto)).getText().toString();
							
							
							Intent in = new Intent(getApplicationContext(), SensorChartActivity.class);
							in.putExtra("title", name);
							in.putExtra("unidad", unidad);
							in.putExtra("id",id_sensor);
							startActivity(in);
							
							// Starting new intent
//							Intent in = new Intent(getApplicationContext(), SensoresSimpleItemActivity.class);
//							in.putExtra(TAG_NAME, name);
//							in.putExtra(TAG_VALOR_ACTUAL, cost);
//							in.putExtra(TAG_HABITACION_NRO, description);
//							startActivity(in);
		
						}
					});
			}else{
				//setContentView(R.layout.error_obteniendo_datos);
			}
			
			//deshabilitar barra progreso
			super.onPostExecute(results);
	        progressDialog.dismiss();
	        
	        if(results != null && results.size() <= 0){
	        	Toast.makeText(SensoresActivity.this, "No se obtuvo información para listar" , Toast.LENGTH_LONG).show();
	        }
	        
			
		}
		  
		  @Override
		protected void onCancelled() {
			Toast.makeText(SensoresActivity.this, "Error: La conexion no es la adecuada para recuperar los datos (cod 1000-M)" , Toast.LENGTH_SHORT).show();
			super.onCancelled();
		}
		
    }
	
	
	
	
	
	
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensores);
	
		
		new GetEstadoSensores().execute();

		
		

	}

}